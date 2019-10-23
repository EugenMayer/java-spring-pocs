package de.kontextwork.poc.spring.blaze.subject;

import com.blazebit.persistence.view.EntityViewSetting;
import com.github.javafaker.Faker;
import com.google.common.base.Stopwatch;
import de.kontextwork.poc.spring.blaze.core.*;
import de.kontextwork.poc.spring.blaze.subject.model.domain.*;
import de.kontextwork.poc.spring.blaze.subject.model.jpa.*;
import de.kontextwork.poc.spring.blaze.subject.model.jpa.privilege.GlobalPrivilege;
import de.kontextwork.poc.spring.blaze.subject.model.jpa.privilege.RealmPrivilege;
import de.kontextwork.poc.spring.blaze.subject.model.jpa.role.*;
import de.kontextwork.poc.spring.blaze.subject.model.jpa.subject.Group;
import de.kontextwork.poc.spring.blaze.subject.model.jpa.subject.Realm;
import de.kontextwork.poc.spring.configuration.BlazePersistenceConfiguration;
import de.kontextwork.poc.spring.configuration.JpaBlazeConfiguration;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalUnit;
import java.util.*;
import java.util.concurrent.TimeUnit;
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

  private RealmPrivilege someRealmPrivilege;
  private RealmPrivilege anotherRealmPrivilege;

  private GlobalPrivilege anotherGlobalPrivilege;
  private GlobalPrivilege someGlobalPrivilege;
  private GlobalPrivilege adminUniqueGlobalPrivilege;

  private GlobalRole globalRoleUser;
  private GlobalRole globalRoleAdmin;
  private GlobalRole globalRoleModerator;

  private User userBob;
  private User userTim;
  private User userJim;

  private Group groupGlobalAdmins;
  private Group groupGlobalModerators;

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
    groupGlobalAdmins = subjectService.create(new Group("Global Admins", randomTeam()));
    groupGlobalModerators = subjectService.create(new Group("Global Moderators", randomTeam()));

    userBob = subjectService.create(new User("Bob", "Smith"));
    userTim = subjectService.create(new User("Tim", "Smith"));
    userJim = subjectService.create(new User("Jim", "Smith"));

    someGlobalPrivilege = subjectService.create(new GlobalPrivilege("SOME_GLOBAL_PRIVILEGE"));
    anotherGlobalPrivilege = subjectService.create(new GlobalPrivilege("ANOTHER_GLOBAL_PRIVILEGE"));
    adminUniqueGlobalPrivilege = subjectService.create(new GlobalPrivilege("ADMIN_UNIQUE_PRIVILEGE"));

    globalRoleUser = subjectService.create(
      new GlobalRole("ROLE_USER", Set.of(someGlobalPrivilege, anotherGlobalPrivilege)));

    globalRoleAdmin = subjectService.create(
      new GlobalRole("ROLE_ADMINISTRATOR",
        Set.of(someGlobalPrivilege, anotherGlobalPrivilege, adminUniqueGlobalPrivilege)));

    globalRoleModerator = subjectService.create(
      new GlobalRole("ROLE_MODERATOR", Set.of(someGlobalPrivilege, anotherGlobalPrivilege)));

    subjectService.assign(globalRoleUser, userBob);
    subjectService.assign(globalRoleModerator, userTim);
    subjectService.assign(globalRoleAdmin, userJim);

    subjectService.assign(globalRoleAdmin, groupGlobalAdmins);
    subjectService.assign(globalRoleModerator, groupGlobalModerators);

    /// Realm Init ///

    final Group groupRealmAdmins = subjectService.create(new Group("Realm Admins", randomTeam()));
    final Group groupRealmModerators = subjectService.create(new Group("Realm Moderators", randomTeam()));

    final Realm realmRed = subjectService.create(new Realm("Red"));
    final Realm realmGreen = subjectService.create(new Realm("Green"));
    final Realm realmBlue = subjectService.create(new Realm("Blue"));

    someRealmPrivilege = subjectService.create(new RealmPrivilege("SOME_REALM_PRIVILEGE"));
    anotherRealmPrivilege = subjectService.create(new RealmPrivilege("ANOTHER_REALM_PRIVILEGE"));

    final RealmRole realmRoleUser =
      subjectService.create(new RealmRole("ROLE_USER", Set.of(someRealmPrivilege, anotherRealmPrivilege)));

    final RealmRole realmRoleAdmin =
      subjectService.create(new RealmRole("ROLE_ADMINISTRATOR", Set.of(someRealmPrivilege, anotherRealmPrivilege)));

    final RealmRole realmRoleModerator =
      subjectService.create(new RealmRole("ROLE_MODERATOR", Set.of(someRealmPrivilege, anotherRealmPrivilege)));

    subjectService.assign(realmRed, realmRoleUser, userTim);
    subjectService.assign(realmGreen, realmRoleUser, userBob);
    subjectService.assign(realmBlue, realmRoleUser, userJim);

    subjectService.assign(realmRoleAdmin, groupRealmAdmins);
    subjectService.assign(realmRoleModerator, groupRealmModerators);
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

  @Test
  @DisplayName("Should approve that user Jim has Privilege '@ParameterizedTest' granted")
  @Sql(
    statements = "alter table subject_user modify uid bigint auto_increment;",
    executionPhase = ExecutionPhase.BEFORE_TEST_METHOD
  )
  void shouldApproveThatUserJimHasPrivilegeAdminUniquePrivilegeGranted()
  {
    // Jim is member of "Global Admins" Group which inherits the "ADMIN_UNIQUE_PRIVILEGE"

    Stopwatch stopwatch = Stopwatch.createUnstarted();

    stopwatch.start();
    for (int i = 0; i < 1000; i++) {
      assertThat(subjectService.hasSubjectPrivilege_fromSubject(userJim, "ADMIN_UNIQUE_PRIVILEGE")).isTrue();
    }
    stopwatch.stop();
    System.out.println(String.format("Variant from Subject took %d ms.", stopwatch.elapsed(TimeUnit.MILLISECONDS)));

    /*stopwatch.start();
    for (int i = 0; i < 100; i++) {
      assertThat(subjectService.hasSubjectPrivilege_fromPrivilege(userJim, "ADMIN_UNIQUE_PRIVILEGE"))
        .isTrue();
    }
    stopwatch.stop();
    System.out.println(String.format("Variant from Privilege took %d ms.", stopwatch.elapsed(TimeUnit.MILLISECONDS)));

    stopwatch.start();
    for (int i = 0; i < 100; i++) {
      assertThat(subjectService.hasSubjectPrivilege_fromGlobalRoleMembership(userJim, "ADMIN_UNIQUE_PRIVILEGE"))
        .isTrue();
    }
    stopwatch.stop();
    System.out.println(String.format("Variant from Role Membership took %d ms.",
      stopwatch.elapsed(TimeUnit.MILLISECONDS)));*/
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