package de.kontextwork.poc.spring.blaze.fullapp.subject;

import com.blazebit.persistence.view.EntityViewSetting;
import com.github.javafaker.Faker;
import de.kontextwork.poc.spring.blaze.core.*;
import de.kontextwork.poc.spring.blaze.fullapp.privilege.PrivilegeService;
import de.kontextwork.poc.spring.blaze.fullapp.realm.RealmService;
import de.kontextwork.poc.spring.blaze.fullapp.realm.model.view.RealmIdView;
import de.kontextwork.poc.spring.blaze.fullapp.role.RoleService;
import de.kontextwork.poc.spring.blaze.fullapp.role.model.jpa.*;
import de.kontextwork.poc.spring.blaze.fullapp.role.model.view.RoleIdView;
import de.kontextwork.poc.spring.blaze.fullapp.rolemembership.GlobalRoleMembershipService;
import de.kontextwork.poc.spring.blaze.fullapp.rolemembership.RealmRoleMembershipService;
import de.kontextwork.poc.spring.blaze.fullapp.subject.group.GroupService;
import de.kontextwork.poc.spring.blaze.fullapp.subject.group.model.view.GroupView;
import de.kontextwork.poc.spring.blaze.fullapp.subject.model.view.*;
import de.kontextwork.poc.spring.blaze.fullapp.privilege.model.jpa.GlobalPrivilege;
import de.kontextwork.poc.spring.blaze.fullapp.privilege.model.jpa.RealmPrivilege;
import de.kontextwork.poc.spring.blaze.fullapp.subject.group.model.jpa.Group;
import de.kontextwork.poc.spring.blaze.fullapp.realm.model.jpa.Realm;
import de.kontextwork.poc.spring.blaze.fullapp.subject.user.UserSerivce;
import de.kontextwork.poc.spring.blaze.fullapp.subject.user.model.jpa.User;
import de.kontextwork.poc.spring.blaze.fullapp.subject.user.model.view.UserView;
import de.kontextwork.poc.spring.configuration.BlazePersistenceConfiguration;
import de.kontextwork.poc.spring.configuration.JpaBlazeConfiguration;
import java.util.*;
import java.util.function.IntFunction;
import java.util.stream.*;
import org.junit.jupiter.api.*;
import org.modelmapper.ModelMapper;
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
  UserSerivce.class,
  GroupService.class,
  PrivilegeService.class,
  RealmService.class,
  RoleService.class,
  RealmRoleMembershipService.class,
  GlobalRoleMembershipService.class,
  JpaBlazeConfiguration.class,
  BlazePersistenceConfiguration.class,
  PageableEntityViewRepository.class,
  RegularEntityViewRepository.class,
  EntityViewDtoConverter.class,
  ModelMapper.class
})
@AutoConfigureDataJdbc
@DataJpaTest(properties = {"blazepersistance.enabled=true"})
class SubjectServiceTest
{
  private final Faker faker = new Faker(new Random(0));

  private final IntFunction<User> randomUser = i -> new User(faker.name().firstName(), faker.name().lastName());

  @Autowired
  private SubjectService subjectService;

  @Autowired
  private RealmRoleMembershipService realmRoleMembershipService;

  @Autowired
  private GlobalRoleMembershipService globalRoleMembershipService;

  @Autowired
  private UserSerivce userSerivce;

  @Autowired
  private GroupService groupService;

  @Autowired
  private PrivilegeService privilegeService;

  @Autowired
  private RealmService realmService;

  @Autowired
  private RoleService roleService;

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

    someGlobalPrivilege = privilegeService.create(new GlobalPrivilege("SOME_GLOBAL_PRIVILEGE"));
    anotherGlobalPrivilege = privilegeService.create(new GlobalPrivilege("ANOTHER_GLOBAL_PRIVILEGE"));
    adminUniqueGlobalPrivilege = privilegeService.create(new GlobalPrivilege("ADMIN_UNIQUE_PRIVILEGE"));

    globalRoleUser = roleService.create(
      new GlobalRole("ROLE_USER", Set.of(someGlobalPrivilege, anotherGlobalPrivilege)));

    globalRoleAdmin = roleService.create(
      new GlobalRole("ROLE_ADMINISTRATOR",
        Set.of(someGlobalPrivilege, anotherGlobalPrivilege, adminUniqueGlobalPrivilege)));

    globalRoleModerator = roleService.create(
      new GlobalRole("ROLE_MODERATOR", Set.of(someGlobalPrivilege, anotherGlobalPrivilege)));

    globalRoleMembershipService.assign(globalRoleUser, userBob);
    globalRoleMembershipService.assign(globalRoleModerator, userTim);
    globalRoleMembershipService.assign(globalRoleAdmin, userJim);

    globalRoleMembershipService.assign(globalRoleAdmin, groupGlobalAdmins);
    globalRoleMembershipService.assign(globalRoleModerator, groupGlobalModerators);

    /// Realm Init ///

    final Group groupRealmAdmins = subjectService.create(new Group("Realm Admins", randomTeam()));
    final Group groupRealmModerators = subjectService.create(new Group("Realm Moderators", randomTeam()));

    final Realm realmRed = realmService.create(new Realm("Red"));
    final Realm realmGreen = realmService.create(new Realm("Green"));
    final Realm realmBlue = realmService.create(new Realm("Blue"));

    someRealmPrivilege = privilegeService.create(new RealmPrivilege("SOME_REALM_PRIVILEGE"));
    anotherRealmPrivilege = privilegeService.create(new RealmPrivilege("ANOTHER_REALM_PRIVILEGE"));

    final RealmRole realmRoleUser =
      roleService.create(new RealmRole("ROLE_USER", Set.of(someRealmPrivilege, anotherRealmPrivilege)));

    final RealmRole realmRoleAdmin =
      roleService.create(new RealmRole("ROLE_ADMINISTRATOR", Set.of(someRealmPrivilege, anotherRealmPrivilege)));

    final RealmRole realmRoleModerator =
      roleService.create(new RealmRole("ROLE_MODERATOR", Set.of(someRealmPrivilege, anotherRealmPrivilege)));

    realmRoleMembershipService.assign(realmRed, realmRoleUser, userTim);
    realmRoleMembershipService.assign(realmGreen, realmRoleUser, userBob);
    realmRoleMembershipService.assign(realmBlue, realmRoleUser, userJim);

    globalRoleMembershipService.assign(realmRoleAdmin, groupRealmAdmins);
    globalRoleMembershipService.assign(realmRoleModerator, groupRealmModerators);
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
    var userSetting = EntityViewSettingFactory.create(UserView.class, userPageRequest);
    final Page<UserView> users = userSerivce.findAll(userSetting, userPageRequest);

    // Σ(3 Users, 4 * 5 random group members)
    assertThat(users.getNumberOfElements()).isEqualTo(23);

    // Should resolve paged Groups
    PageRequest groupPageRequest = PageRequest.of(0, 50, Direction.DESC, "id", "name");
    var groupSetting = EntityViewSettingFactory.create(GroupView.class, groupPageRequest);
    final Page<GroupView> groups = groupService.findAll(groupSetting, groupPageRequest);

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
    final Set<SubjectView> subjects = subjectService.findAll(subjectSetting);

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
    final Page<SubjectView> subjects = subjectService.findAll(subjectSetting, pageRequest);

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
    var setting = EntityViewSettingFactory.create(UserView.class, userPageRequest);
    setting.addViewFilter("USER_IN_GLOBAL_ROLE_ADMINISTRATOR");
    final Page<UserView> users = userSerivce.findAll(setting, userPageRequest);

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
    var setting = EntityViewSettingFactory.create(UserView.class, userPageRequest);
    setting.addViewFilter("USER_IN_GLOBAL_ROLE_MODERATOR");
    final Page<UserView> users = userSerivce.findAll(setting, userPageRequest);

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
    var setting = EntityViewSettingFactory.create(UserView.class, userPageRequest);
    setting.addViewFilter("USER_IN_GLOBAL_ROLE_USER");
    final Page<UserView> users = userSerivce.findAll(setting, userPageRequest);

    // Σ(1 Global User)
    assertThat(users.getNumberOfElements()).isEqualTo(1);
  }

  @Test
  @DisplayName("Should approve that user Jim has Privilege 'ADMIN_UNIQUE_PRIVILEGE' granted")
  @Sql(
    statements = "alter table subject_user modify uid bigint auto_increment;",
    executionPhase = ExecutionPhase.BEFORE_TEST_METHOD
  )
  void shouldApproveThatUserJimHasPrivilegeAdminUniquePrivilegeGranted()
  {
    assertThat(subjectService.hasPrivilegeViaPrivilege(userJim, "ADMIN_UNIQUE_PRIVILEGE")).isTrue();
    // Jim is member of "Global Admins" Group which inherits the "ADMIN_UNIQUE_PRIVILEGE"
    assertThat(subjectService.hasPrivilegeViaSubject(userJim, "ADMIN_UNIQUE_PRIVILEGE")).isTrue();
    assertThat(subjectService.hasPrivilegeViaGlobalRoleMembership(userJim, "ADMIN_UNIQUE_PRIVILEGE")).isTrue();

    assertThat(subjectService.hasPrivilegeViaSubject(userJim, "UNKNOWN_PRIVILEGE")).isFalse();
    assertThat(subjectService.hasPrivilegeViaPrivilege(userJim, "UNKNOWN_PRIVILEGE")).isFalse();
    assertThat(subjectService.hasPrivilegeViaGlobalRoleMembership(userJim, "UNKNOWN_PRIVILEGE")).isFalse();
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