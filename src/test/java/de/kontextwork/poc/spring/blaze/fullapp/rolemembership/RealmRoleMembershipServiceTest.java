package de.kontextwork.poc.spring.blaze.fullapp.rolemembership;

import com.blazebit.persistence.view.EntityViewManager;
import de.kontextwork.poc.spring.blaze.core.*;
import de.kontextwork.poc.spring.blaze.fullapp.realm.RealmService;
import de.kontextwork.poc.spring.blaze.fullapp.role.RoleService;
import de.kontextwork.poc.spring.blaze.fullapp.role.model.jpa.GlobalRole;
import de.kontextwork.poc.spring.blaze.fullapp.rolemembership.model.view.*;
import de.kontextwork.poc.spring.blaze.fullapp.subject.SubjectService;
import de.kontextwork.poc.spring.blaze.fullapp.subject.user.model.jpa.User;
import de.kontextwork.poc.spring.blaze.fullapp.realm.model.jpa.Realm;
import de.kontextwork.poc.spring.configuration.BlazePersistenceConfiguration;
import de.kontextwork.poc.spring.configuration.JpaBlazeConfiguration;
import java.util.Set;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.AutoConfigureDataJdbc;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;

import static org.assertj.core.api.Assertions.assertThat;

@Import({
  SubjectService.class,
  RealmService.class,
  RoleService.class,
  RealmRoleMembershipService.class,
  JpaBlazeConfiguration.class,
  BlazePersistenceConfiguration.class,
  PageableEntityViewRepository.class,
  RegularEntityViewRepository.class,
  EntityViewDtoConverter.class,
  ModelMapper.class
})
@AutoConfigureDataJdbc
@DataJpaTest(properties = {"blazepersistance.enabled=true"})
class RealmRoleMembershipServiceTest
{

  @Autowired
  private SubjectService subjectService;

  @Autowired
  private RealmService realmService;

  @Autowired
  private RoleService roleService;

  @Autowired
  EntityViewManager entityViewManager;

  @Autowired
  EntityManager entityManager;


  @Autowired
  private RealmRoleMembershipService realmRoleMembershipService;

  @Test
  @Sql(
    statements = "alter table subject_user modify uid bigint auto_increment;",
    executionPhase = ExecutionPhase.BEFORE_TEST_METHOD
  )
  void createMembership()
  {
    User user = subjectService.create(new User("John", "Doe"));
    Realm realm = realmService.create(new Realm("Random"));
    GlobalRole role = roleService.create(new GlobalRole("SOME_ROLE", Set.of()));

    final RealmRoleMembershipCreateView realmRoleMembershipCreateView = entityViewManager.create(
      RealmRoleMembershipCreateView.class);

    realmRoleMembershipCreateView.getId().setRealmId(realm.getId());
    realmRoleMembershipCreateView.getId().setRoleId(role.getId());
    realmRoleMembershipCreateView.getId().setSubjectId(user.getId());

    realmRoleMembershipCreateView.setRole(role);
    realmRoleMembershipCreateView.setRealm(realm);
    realmRoleMembershipCreateView.setSubject(user);

    realmRoleMembershipService.createUntouched(realmRoleMembershipCreateView);
    assertThat( realmRoleMembershipService.findAll().size()).isNotZero();
  }

  @Test
  @Sql(
    statements = "alter table subject_user modify uid bigint auto_increment;",
    executionPhase = ExecutionPhase.BEFORE_TEST_METHOD
  )
  void createMembershipViewEmbeddedId()
  {
    User user = subjectService.create(new User("John", "Doe"));
    Realm realm = realmService.create(new Realm("Random"));
    GlobalRole role = roleService.create(new GlobalRole("SOME_ROLE", Set.of()));

    final RealmRoleMembershipEmbeddedIdCreateView realmRoleMembershipEmbeddedIdCreateView = entityViewManager.create(
      RealmRoleMembershipEmbeddedIdCreateView.class);

    realmRoleMembershipEmbeddedIdCreateView.setRealmId(realm.getId());
    realmRoleMembershipEmbeddedIdCreateView.setRoleId(role.getId());
    realmRoleMembershipEmbeddedIdCreateView.setSubjectId(user.getId());

    realmRoleMembershipService.createByEmbeddedId(realmRoleMembershipEmbeddedIdCreateView);
    assertThat( realmRoleMembershipService.findAll().size()).isNotZero();
  }
//
//  @Test
//  @DisplayName("Should create RealmRoleMembership via EntityView")
//  @Sql(
//    statements = "alter table subject_user modify uid bigint auto_increment;",
//    executionPhase = ExecutionPhase.BEFORE_TEST_METHOD
//  )
//  void shouldCreateRealmRoleMembershipViaEntityView()
//  {
//    User user = subjectService.create(new User("John", "Doe"));
//    Realm realm = realmService.create(new Realm("Random"));
//    GlobalRole role = roleService.create(new GlobalRole("SOME_ROLE", Set.of()));
//
//    RealmRoleMembershipEmbeddedIdView idView = RealmRoleMembershipEmbeddedIdViewDTO.builder()
//      .realmId(realm.getId())
//      .roleId(role.getId())
//      .subjectId(user.getId())
//      .build();
//
//    RealmRoleMembershipCreateView createView = RealmRoleMembershipCreateViewDTO.builder()
//      .id(idView)
//      .build();
//
//    realmRoleMembershipService.create(createView);
//  }

}