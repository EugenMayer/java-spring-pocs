package de.kontextwork.poc.spring.blaze.subject;

import com.blazebit.persistence.view.EntityViewSetting;
import com.github.javafaker.Faker;
import de.kontextwork.poc.spring.blaze.core.*;
import de.kontextwork.poc.spring.blaze.subject.model.domain.*;
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
import org.springframework.test.annotation.Rollback;
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

  /**
   * This creates the data basis containing 3 {@link Role}, 6 {@link Group} and 23 {@link User} entities that act as
   * our default test scenario:
   *
   * <ul>
   *   <li>Group "Admins" containing 5 randomly generated members in global "ADMINISTRATOR" role</li>
   *   <li>Group "Moderators" containing 5 randomly generated members in global "MODERATOR" role</li>
   *   <li>User "Bob Smith" in global "USER" role</li>
   *   <li>User "Tim Smith" in global "MODERATOR" role</li>
   *   <li>User "Jim Smith" in global "ADMINISTRATOR" role</li>
   *   <li>Role "ADMINISTRATOR"</li>
   *   <li>Role "MODERATOR"</li>
   *   <li>Role "USER"</li>
   * </ul>
   * <ul>
   *   <li>Realm "Red"</li>
   *   <ul>
   *     <li>User "Tim Smith" in realm "ADMINISTRATOR" role</li>
   *   </ul>
   *   <li>Realm "Green"</li>
   *   <ul>
   *     <li>Group "Realm Green Admins" containing 5 randomly generated members in realm "ADMINISTRATOR" role</li>
   *   </ul>
   *   <li>Realm "Green"</li>
   *   <ul>
   *     <li>Group "Realm Blue Users" containing 5 randomly generated members in realm "USER" role</li>
   *   </ul>
   * </ul>
   */
  @BeforeEach
  public void setup()
  {
    final Group groupAdmins = subjectService.create(new Group("Admins", randomTeam()));
    final Group groupModerators = subjectService.create(new Group("Moderators", randomTeam()));

    final User userBob = subjectService.create(new User("Bob", "Smith"));
    final User userTim = subjectService.create(new User("Tim", "Smith"));
    final User userJim = subjectService.create(new User("Jim", "Smith"));

    final Role roleUser = subjectService.create(new Role("ROLE_USER"));
    final Role roleAdmin = subjectService.create(new Role("ROLE_ADMINISTRATOR"));
    final Role roleModerator = subjectService.create(new Role("ROLE_MODERATOR"));

    final Realm realmRed = subjectService.create(new Realm("Red"));
    final Realm realmGreen = subjectService.create(new Realm("Green"));
    final Realm realmBlue = subjectService.create(new Realm("Blue"));

    subjectService.assign(roleUser, userBob);
    subjectService.assign(roleModerator, userTim);
    subjectService.assign(roleAdmin, userJim);

    subjectService.assign(roleAdmin, groupAdmins);
    subjectService.assign(roleModerator, groupModerators);

    subjectService.assign(realmRed, roleAdmin, userTim);

    final Group realmGreenAdminGroup = subjectService.create(new Group("Realm Green Admins", randomTeam()));
    final Group realmBlueUserGroup = subjectService.create(new Group("Realm Blue Users", randomTeam()));

    subjectService.assign(realmGreen, roleModerator, realmGreenAdminGroup);
    subjectService.assign(realmBlue, roleUser, realmBlueUserGroup);
  }

  @Test
  @Rollback
  @DisplayName("Should create Users and Groups as Subjects")
  @Sql(
    statements = "alter table subject_user modify uid bigint auto_increment;",
    executionPhase = ExecutionPhase.BEFORE_TEST_METHOD
  )
  void shouldCreateUsersAndGroupsAsSubjects()
  {
    // Should resolve paged Users
    PageRequest userPageRequest = PageRequest.of(0, 50, Direction.DESC, "id", "lastName");
    var userSetting = EntityViewSettingFactory.create(SubjectUserView.class, userPageRequest);
    final Page<SubjectUserView> users = subjectService.getSubjectUsers(userSetting, userPageRequest);

    // Σ(3 Users, 4 * 5 random group members)
    assertThat(users.getNumberOfElements()).isEqualTo(23);

    // Should resolve paged Groups
    PageRequest groupPageRequest = PageRequest.of(0, 50, Direction.DESC, "id", "name");
    var groupSetting = EntityViewSettingFactory.create(SubjectGroupView.class, groupPageRequest);
    final Page<SubjectGroupView> groups = subjectService.getSubjectGroups(groupSetting, groupPageRequest);

    // Σ(4 Groups)
    assertThat(groups.getNumberOfElements()).isEqualTo(4);
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

    // Σ(3 Users, 4 * 5 Group members, 4 Groups)
    assertThat(subjects).hasSize(27);
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

    // Σ(3 Users, 4 * 5 Group members, 4 Groups)
    assertThat(subjects.getNumberOfElements()).isEqualTo(27);
  }

  @Test
  @DisplayName("Should resolve Users in Role Administrator")
  @Sql(
    statements = "alter table subject_user modify uid bigint auto_increment;",
    executionPhase = ExecutionPhase.BEFORE_TEST_METHOD
  )
  void shouldResolveUsersInRoleAdministrator()
  {
    PageRequest userPageRequest = PageRequest.of(0, 50, Direction.DESC, "id", "lastName");
    var setting = EntityViewSettingFactory.create(SubjectUserView.class, userPageRequest);
    setting.addViewFilter("USER_IN_GLOBAL_ROLE_ADMINISTRATOR");
    final Page<SubjectUserView> users = subjectService.getSubjectUsers(setting, userPageRequest);

    // Σ(1 Global Admin, 5 Members of Group "Admins")
    assertThat(users.getNumberOfElements()).isEqualTo(6);
  }

  @Test
  @DisplayName("Should resolve Users in Role Moderator")
  @Sql(
    statements = "alter table subject_user modify uid bigint auto_increment;",
    executionPhase = ExecutionPhase.BEFORE_TEST_METHOD
  )
  void shouldResolveUsersInRoleModerator()
  {
    PageRequest userPageRequest = PageRequest.of(0, 50, Direction.DESC, "id", "lastName");
    var setting = EntityViewSettingFactory.create(SubjectUserView.class, userPageRequest);
    setting.addViewFilter("USER_IN_GLOBAL_ROLE_MODERATOR");
    final Page<SubjectUserView> users = subjectService.getSubjectUsers(setting, userPageRequest);

    // Σ(1 Global Moderator, 5 Members of Group "Moderators")
    assertThat(users.getNumberOfElements()).isEqualTo(6);
  }

  @Test
  @DisplayName("Should resolve Users in Role User")
  @Sql(
    statements = "alter table subject_user modify uid bigint auto_increment;",
    executionPhase = ExecutionPhase.BEFORE_TEST_METHOD
  )
  void shouldResolveUsersInRoleUser()
  {
    PageRequest userPageRequest = PageRequest.of(0, 50, Direction.DESC, "id", "lastName");
    var setting = EntityViewSettingFactory.create(SubjectUserView.class, userPageRequest);
    setting.addViewFilter("USER_IN_GLOBAL_ROLE_USER");
    final Page<SubjectUserView> users = subjectService.getSubjectUsers(setting, userPageRequest);

    // Σ(1 Global User)
    assertThat(users.getNumberOfElements()).isEqualTo(1);
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