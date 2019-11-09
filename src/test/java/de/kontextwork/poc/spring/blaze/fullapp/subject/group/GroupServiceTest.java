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
import de.kontextwork.poc.spring.blaze.fullapp.testUtils.scenario.GroupScenarioCreator;
import de.kontextwork.poc.spring.blaze.fullapp.testUtils.scenario.UserScenarioCreator;
import de.kontextwork.poc.spring.configuration.BlazePersistenceConfiguration;
import de.kontextwork.poc.spring.configuration.JpaBlazeConfiguration;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.test.autoconfigure.data.jdbc.AutoConfigureDataJdbc;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("SameParameterValue")
@Import({
  UserSerivce.class,
  GroupService.class,
  JpaBlazeConfiguration.class,
  BlazePersistenceConfiguration.class,
  PageableEntityViewRepository.class,
  RegularEntityViewRepository.class,
  EntityViewDtoConverter.class,
  ModelMapper.class,
  GroupScenarioCreator.class,
  UserScenarioCreator.class
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
  GroupScenarioCreator groupScenarioCreator;

  @Autowired
  UserScenarioCreator userScenarioCreator;

  @Autowired
  EntityViewManager entityViewManager;

  @Test
  @Sql(
    statements = "alter table subject_user modify uid bigint auto_increment;",
    executionPhase = ExecutionPhase.BEFORE_TEST_METHOD
  )
  void create()
  {
    GroupIdView groupIdView = groupScenarioCreator.createGroup("TestGroup", "testMachine");

    GroupEntireView groupEntireView = groupScenarioCreator.getFullGroup(groupIdView);
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
    GroupIdView groupIdView = groupScenarioCreator.createGroup("TestGroup", "testMachine");
    UserIdView userIdView = userScenarioCreator.createUser("Max", "Mustermann");

    groupScenarioCreator.limitUsersTo(groupIdView, userIdView);

    GroupEntireView groupEntireView = groupScenarioCreator.getFullGroup(groupIdView);
    assertThat(groupEntireView.getMembers()).hasSize(1);
  }

  @Test
  @Sql(
    statements = "alter table subject_user modify uid bigint auto_increment;",
    executionPhase = ExecutionPhase.BEFORE_TEST_METHOD
  )
  void removeMember()
  {
    GroupIdView groupIdView = groupScenarioCreator.createGroup("TestGroup", "testMachine");
    UserIdView userMax = userScenarioCreator.createUser("Max", "Mustermann");
    UserIdView userDax = userScenarioCreator.createUser("Dax", "Daxmann");

    groupScenarioCreator.limitUsersTo(groupIdView, userMax, userDax);

    assertThat(groupScenarioCreator.getFullGroup(groupIdView).getMembers()).hasSize(2);

    groupService.removeMember(groupIdView.getId(), userMax);
    assertThat(groupScenarioCreator.getFullGroup(groupIdView).getMembers()).hasSize(1);


    groupService.removeMember(groupIdView.getId(), userDax);
    assertThat(groupScenarioCreator.getFullGroup(groupIdView).getMembers()).hasSize(0);
  }
}