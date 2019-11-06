package de.kontextwork.poc.spring.blaze.fullapp.subject;

import com.blazebit.persistence.view.EntityViewSetting;
import de.kontextwork.poc.spring.blaze.core.*;
import de.kontextwork.poc.spring.blaze.fullapp.privilege.PrivilegeService;
import de.kontextwork.poc.spring.blaze.fullapp.realm.RealmService;
import de.kontextwork.poc.spring.blaze.fullapp.realm.model.jpa.Realm;
import de.kontextwork.poc.spring.blaze.fullapp.role.RoleService;
import de.kontextwork.poc.spring.blaze.fullapp.role.model.jpa.GlobalRole;
import de.kontextwork.poc.spring.blaze.fullapp.role.model.jpa.RealmRole;
import de.kontextwork.poc.spring.blaze.fullapp.rolemembership.GlobalRoleMembershipService;
import de.kontextwork.poc.spring.blaze.fullapp.rolemembership.RealmRoleMembershipService;
import de.kontextwork.poc.spring.blaze.fullapp.rolemembership.model.view.*;
import de.kontextwork.poc.spring.blaze.fullapp.subject.group.GroupService;
import de.kontextwork.poc.spring.blaze.fullapp.subject.group.model.jpa.Group;
import de.kontextwork.poc.spring.blaze.fullapp.subject.group.model.view.GroupExcerptView;
import de.kontextwork.poc.spring.blaze.fullapp.subject.user.UserSerivce;
import de.kontextwork.poc.spring.blaze.fullapp.subject.user.model.jpa.User;
import de.kontextwork.poc.spring.blaze.fullapp.subject.user.model.view.UserExcerptView;
import de.kontextwork.poc.spring.configuration.BlazePersistenceConfiguration;
import de.kontextwork.poc.spring.configuration.JpaBlazeConfiguration;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.AutoConfigureDataJdbc;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
public class InheritanceTest
{
  @Autowired
  private RoleService roleService;

  @Autowired
  private SubjectService subjectService;


  @Autowired
  private RealmService realmService;

  @Autowired
  private RealmRoleMembershipService realmRoleMembershipService;

  @Test
  @Sql(
    statements = "alter table subject_user modify uid bigint auto_increment;",
    executionPhase = ExecutionPhase.BEFORE_TEST_METHOD
  )
  void testInheritance()
  {
    final User userRed = subjectService.create(new User("Bob", "Smith"));
    final Group groupBlue = subjectService.create(new Group("Global Admins", new HashSet<>()));
    final Realm realmRed = realmService.create(new Realm("Red"));
    final Realm realmBlue = realmService.create(new Realm("Red"));

    final RealmRole realmRoleRed =
      roleService.create(new RealmRole("TEST_ROLE1", new HashSet<>()));

    final RealmRole realmRoleBlue =
      roleService.create(new RealmRole("TEST_ROLE2", new HashSet<>()));

    realmRoleMembershipService.assign(realmRed, realmRoleRed, userRed);
    realmRoleMembershipService.assign(realmRed, realmRoleBlue, groupBlue);


    Set<RealmRoleMembershipWithUpcastView> memberships =
      realmRoleMembershipService.findAll(EntityViewSetting.create(RealmRoleMembershipWithUpcastView.class));
    assertThat(memberships)
      .anySatisfy(membership -> assertThat(membership.getSubject()).isInstanceOf(UserExcerptView.class)
    );
    assertThat(memberships)
      .anySatisfy(membership -> assertThat(membership.getSubject()).isInstanceOf(GroupExcerptView.class)
    );

    assertThat(memberships)
      .allSatisfy(membership -> {
          assertThat(membership.getSubject()).hasFieldOrProperty("machine").isNotNull();
        }
      );

    memberships = realmRoleMembershipService.findAllByRealm(
      realmRed.getId(), EntityViewSetting.create(RealmRoleMembershipWithUpcastView.class)
    );
    assertThat(memberships)
      .anySatisfy(user -> assertThat(user).isInstanceOf(UserExcerptView.class)
      );
    assertThat(memberships)
      .anySatisfy(user -> assertThat(user).isInstanceOf(GroupExcerptView.class)
      );
  }
}