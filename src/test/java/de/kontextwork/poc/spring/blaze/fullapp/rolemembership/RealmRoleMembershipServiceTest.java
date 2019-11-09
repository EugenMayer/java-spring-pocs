package de.kontextwork.poc.spring.blaze.fullapp.rolemembership;

import com.blazebit.persistence.view.EntityViewManager;
import com.blazebit.persistence.view.EntityViewSetting;
import de.kontextwork.poc.spring.blaze.core.PageableEntityViewRepository;
import de.kontextwork.poc.spring.blaze.core.RegularEntityViewRepository;
import de.kontextwork.poc.spring.blaze.fullapp.realm.RealmService;
import de.kontextwork.poc.spring.blaze.fullapp.realm.model.jpa.Realm;
import de.kontextwork.poc.spring.blaze.fullapp.realm.model.view.RealmIdView;
import de.kontextwork.poc.spring.blaze.fullapp.role.RoleService;
import de.kontextwork.poc.spring.blaze.fullapp.role.model.jpa.RealmRole;
import de.kontextwork.poc.spring.blaze.fullapp.role.model.view.RoleIdView;
import de.kontextwork.poc.spring.blaze.fullapp.rolemembership.model.view.RealmRoleMembershipCreateView;
import de.kontextwork.poc.spring.blaze.fullapp.rolemembership.model.view.RealmRoleMembershipIdView;
import de.kontextwork.poc.spring.blaze.fullapp.subject.SubjectService;
import de.kontextwork.poc.spring.blaze.fullapp.subject.model.view.SubjectIdView;
import de.kontextwork.poc.spring.blaze.fullapp.subject.user.model.jpa.User;
import de.kontextwork.poc.spring.configuration.BlazePersistenceConfiguration;
import de.kontextwork.poc.spring.configuration.JpaBlazeConfiguration;
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

@Import({
  SubjectService.class,
  RealmService.class,
  RoleService.class,
  RealmRoleMembershipService.class,
  JpaBlazeConfiguration.class,
  BlazePersistenceConfiguration.class,
  PageableEntityViewRepository.class,
  RegularEntityViewRepository.class,
  ModelMapper.class
})
@AutoConfigureDataJdbc
@DataJpaTest(properties = {"blazepersistance.enabled=true"})
class RealmRoleMembershipServiceTest
{
  @Autowired
  EntityViewManager entityViewManager;
  @Autowired
  private SubjectService subjectService;
  @Autowired
  private RealmService realmService;

  @Autowired
  private RoleService roleService;

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
    RealmRole role = roleService.create(new RealmRole("SOME_ROLE", Set.of()));

    final SubjectIdView subjectIdView = subjectService.getOneAsIdView(user.getId()).orElseThrow();
    final RealmIdView realmIdView = realmService.getOneAsIdView(realm.getId()).orElseThrow();
    final RoleIdView roleIdView = roleService.getOneAsIdView(role.getId()).orElseThrow();

    RealmRoleMembershipCreateView createView = entityViewManager.create(RealmRoleMembershipCreateView.class);
    createView.setRealm(realmIdView);
    createView.setRole(roleIdView);
    createView.setSubject(subjectIdView);

    realmRoleMembershipService.create(createView);
    assertThat( realmRoleMembershipService.findAll(EntityViewSetting.create(RealmRoleMembershipIdView.class)).size() ).isNotZero();
  }

  @Test
  @Sql(
    statements = "alter table subject_user modify uid bigint auto_increment;",
    executionPhase = ExecutionPhase.BEFORE_TEST_METHOD
  )
  void assign()
  {
    User user = subjectService.create(new User("John", "Doe"));
    Realm realm = realmService.create(new Realm("Random"));
    RealmRole role = roleService.create(new RealmRole("SOME_ROLE", Set.of()));
    realmRoleMembershipService.assign(realm, role, user);
    assertThat( realmRoleMembershipService.findAll(EntityViewSetting.create(RealmRoleMembershipIdView.class)).size() ).isNotZero();
  }
}