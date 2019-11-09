package de.kontextwork.poc.spring.blaze.fullapp.subject.group;

import com.blazebit.persistence.view.EntityViewManager;
import com.blazebit.persistence.view.EntityViewSetting;
import com.google.common.collect.Sets;
import de.kontextwork.poc.spring.blaze.core.*;
import de.kontextwork.poc.spring.blaze.fullapp.privilege.PrivilegeService;
import de.kontextwork.poc.spring.blaze.fullapp.realm.RealmService;
import de.kontextwork.poc.spring.blaze.fullapp.role.RoleService;
import de.kontextwork.poc.spring.blaze.fullapp.rolemembership.GlobalRoleMembershipService;
import de.kontextwork.poc.spring.blaze.fullapp.rolemembership.RealmRoleMembershipService;
import de.kontextwork.poc.spring.blaze.fullapp.subject.SubjectService;
import de.kontextwork.poc.spring.blaze.fullapp.subject.group.model.view.*;
import de.kontextwork.poc.spring.blaze.fullapp.subject.user.UserSerivce;
import de.kontextwork.poc.spring.blaze.fullapp.subject.user.model.view.UserCreateView;
import de.kontextwork.poc.spring.blaze.fullapp.subject.user.model.view.UserIdView;
import de.kontextwork.poc.spring.configuration.BlazePersistenceConfiguration;
import de.kontextwork.poc.spring.configuration.JpaBlazeConfiguration;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.AutoConfigureDataJdbc;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("SameParameterValue")
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
  ModelMapper.class,
})
@AutoConfigureDataJdbc
@DataJpaTest(properties = {"blazepersistance.enabled=true"}, showSql = true)
class GroupServiceTest
{
  @Autowired
  GroupService groupService;

  @Autowired
  UserSerivce userService;


  @Autowired
  EntityViewManager entityViewManager;

  @Test
  @Sql(
    statements = "alter table subject_user modify uid bigint auto_increment;",
    executionPhase = ExecutionPhase.BEFORE_TEST_METHOD
  )
  void create()
  {
    GroupIdView groupIdView = createGroup("TestGroup", "testMachine");

    GroupEntireView groupEntireView = getFullGroup(groupIdView);
    assertThat(groupEntireView.getName()).isEqualTo("TestGroup");
    assertThat(groupEntireView.getMachine()).isEqualTo("testMachine");
  }

  @Test
  @Sql(
    statements = "alter table subject_user modify uid bigint auto_increment;",
    executionPhase = ExecutionPhase.BEFORE_TEST_METHOD
  )
  void limitMembersTo()
  {
    GroupIdView groupIdView = createGroup("TestGroup", "testMachine");
    UserIdView userIdView = createUser("Max", "Mustermann");

    limitUsersTo(groupIdView, userIdView);

    GroupEntireView groupEntireView = getFullGroup(groupIdView);
    assertThat(groupEntireView.getMembers()).hasSize(1);
  }

  @Test
  @Sql(
    statements = "alter table subject_user modify uid bigint auto_increment;",
    executionPhase = ExecutionPhase.BEFORE_TEST_METHOD
  )
  void removeMember()
  {
    GroupIdView groupIdView = createGroup("TestGroup", "testMachine");
    UserIdView userMax = createUser("Max", "Mustermann");
    UserIdView userDax = createUser("Dax", "Daxmann");

    limitUsersTo(groupIdView, userMax, userDax);

    assertThat(getFullGroup(groupIdView).getMembers()).hasSize(2);

    groupService.removeMember(groupIdView.getId(), userMax);
    assertThat(getFullGroup(groupIdView).getMembers()).hasSize(1);


    groupService.removeMember(groupIdView.getId(), userDax);
    assertThat(getFullGroup(groupIdView).getMembers()).hasSize(0);
  }

  GroupIdView createGroup(String name, String machine) {
    GroupCreateView groupCreateView = entityViewManager.create(GroupCreateView.class);
    groupCreateView.setName(name);
    groupCreateView.setGroupMachine(machine);

    return groupService.create(groupCreateView);
  }

  UserIdView createUser(String firstName, String lastName) {
    UserCreateView userCreateView = entityViewManager.create(UserCreateView.class);
    userCreateView.setFirstName(firstName);
    userCreateView.setLastName(lastName);

    return userService.create(userCreateView);
  }

  void limitUsersTo(GroupIdView groupIdView, UserIdView... newMembers) {
    GroupMemberUpdateView groupMemberUpdateView = entityViewManager.getReference(
      GroupMemberUpdateView.class,
      groupIdView.getId()
    );
    groupMemberUpdateView.setMembers(Sets.newHashSet(newMembers));
    groupService.limitMembersTo(groupIdView.getId(), groupMemberUpdateView);
  }

  GroupEntireView getFullGroup(GroupIdView groupIdView) {
    return groupService.getOne(
      EntityViewSetting.create(GroupEntireView.class), groupIdView.getId()
    ).orElseThrow();
  }
}