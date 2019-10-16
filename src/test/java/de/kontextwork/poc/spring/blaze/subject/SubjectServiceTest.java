package de.kontextwork.poc.spring.blaze.subject;

import com.blazebit.persistence.view.EntityViewSetting;
import com.github.javafaker.Faker;
import de.kontextwork.poc.spring.blaze.core.*;
import de.kontextwork.poc.spring.blaze.subject.model.domain.group.GroupSubjectView;
import de.kontextwork.poc.spring.blaze.subject.model.domain.subject.SubjectView;
import de.kontextwork.poc.spring.blaze.subject.model.domain.user.UserSubjectView;
import de.kontextwork.poc.spring.blaze.subject.model.jpa.*;
import de.kontextwork.poc.spring.configuration.BlazePersistenceConfiguration;
import de.kontextwork.poc.spring.configuration.JpaBlazeConfiguration;
import java.util.*;
import java.util.function.IntFunction;
import java.util.stream.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.AutoConfigureDataJdbc;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;

import static org.assertj.core.api.Assertions.assertThat;

@Import({
  SubjectService.class,
  JpaBlazeConfiguration.class,
  BlazePersistenceConfiguration.class,
  PageableEntityViewRepository.class,
  RegularEntityViewRepository.class
})
@AutoConfigureDataJdbc
@DataJpaTest(properties = {"blazepersistance.enabled=true"})
class SubjectServiceTest
{
  private final Faker faker = new Faker(new Random(0));

  private final IntFunction<User> randomUser = i -> new User(faker.name().firstName(), faker.name().lastName());

  @Autowired
  private SubjectService subjectService;

  @BeforeEach
  public void setup()
  {
    final Group groupAdmins = subjectService.create(new Group("Admins", randomTeam()));
    final Group groupModerators = subjectService.create(new Group("Moderators", randomTeam()));

    final User userBob = subjectService.create(new User("Bob", "Smith"));
    final User userTim = subjectService.create(new User("Tim", "Smith"));
    final User userJim = subjectService.create(new User("Jim", "Smith"));

    final Role roleUser = subjectService.create(new Role("ROLE_USER"));
    final Role roleAdmin = subjectService.create(new Role("ROLE_ADMIN"));
    final Role roleModerator = subjectService.create(new Role("ROLE_MODERATOR"));

    subjectService.assign(roleUser, userBob);
    subjectService.assign(roleAdmin, userJim);
    subjectService.assign(roleModerator, userTim);

    subjectService.assign(roleAdmin, groupAdmins);
    subjectService.assign(roleModerator, groupModerators);
  }

  @Test
  @DisplayName("Should create Users and Groups as Subjects")
  @Sql(
    statements = "alter table subject_user modify uid bigint auto_increment;",
    executionPhase = ExecutionPhase.BEFORE_TEST_METHOD
  )
  void shouldCreateUsersAndGroupsAsSubjects()
  {
    // Should resolve paged Users
    PageRequest userPageRequest = PageRequest.of(0, 50, Direction.DESC, "id", "lastName");
    var userSetting = EntityViewSettingFactory.create(UserSubjectView.class, userPageRequest);
    final Page<UserSubjectView> users = subjectService.getUsers(userSetting, userPageRequest);

    // 3 Test Users + 10 Group members
    assertThat(users.getNumberOfElements()).isEqualTo(13);

    // Should resolve paged Groups
    PageRequest groupPageRequest = PageRequest.of(0, 50, Direction.DESC, "id", "name");
    var groupSetting = EntityViewSettingFactory.create(GroupSubjectView.class, groupPageRequest);
    final Page<GroupSubjectView> groups = subjectService.getGroups(groupSetting, groupPageRequest);

    assertThat(groups.getNumberOfElements()).isEqualTo(2);
  }

  @Test
  @DisplayName("Should resolve Subject as List")
  @Sql(
    statements = "alter table subject_user modify uid bigint auto_increment;",
    executionPhase = ExecutionPhase.BEFORE_TEST_METHOD
  )
  void shouldResolveSubjectsAsList()
  {
    var subjectSetting = EntityViewSetting.create(SubjectView.class);
    final Set<SubjectView> subjects = subjectService.getSubjects(subjectSetting);

    assertThat(subjects).hasSize(15);
  }

  @Test
  @DisplayName("Should resolve paginated Subjects")
  @Sql(
    statements = "alter table subject_user modify uid bigint auto_increment;",
    executionPhase = ExecutionPhase.BEFORE_TEST_METHOD
  )
  void shouldResolvePaginatedSubjects()
  {
    PageRequest pageRequest = PageRequest.of(0, 50, Direction.DESC, "id");
    var subjectSetting = EntityViewSettingFactory.create(SubjectView.class, pageRequest);
    final Page<SubjectView> subjects = subjectService.getSubjects(subjectSetting, pageRequest);

    assertThat(subjects.getNumberOfElements()).isEqualTo(15);
  }

  @Test
  @DisplayName("Should resolve Subjects with given Role")
  @Sql(
    statements = "alter table subject_user modify uid bigint auto_increment;",
    executionPhase = ExecutionPhase.BEFORE_TEST_METHOD
  )
  void shouldResolveSubjectsWithGivenRole()
  {
    PageRequest pageRequest = PageRequest.of(0, 50, Direction.DESC, "id");

    var userRoleSetting = EntityViewSettingFactory.create(SubjectView.class, pageRequest);
    userRoleSetting.addViewFilter("userRoleFiler");

    final Page<SubjectView> subjects = subjectService.getSubjects(userRoleSetting, pageRequest);
    assertThat(subjects.getNumberOfElements()).isEqualTo(1);
  }

  private Set<User> randomTeam()
  {
    return IntStream.rangeClosed(1, 5)
      .mapToObj(randomUser)
      .map(subjectService::create)
      .peek(usr -> assertThat(usr.getUid()).isNotNull().isGreaterThan(0))
      .collect(Collectors.toSet());
  }
}