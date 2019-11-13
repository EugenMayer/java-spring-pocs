package de.kontextwork.poc.spring.blaze;

import com.blazebit.persistence.view.EntityViewManager;
import de.kontextwork.poc.spring.blaze.core.PageableEntityViewRepository;
import de.kontextwork.poc.spring.blaze.core.RegularEntityViewRepository;
import de.kontextwork.poc.spring.blaze.fullapp.realm.RealmService;
import de.kontextwork.poc.spring.blaze.fullapp.realm.model.jpa.Realm;
import de.kontextwork.poc.spring.blaze.fullapp.realm.model.view.RealmIdView;
import de.kontextwork.poc.spring.blaze.fullapp.role.RoleRepository;
import de.kontextwork.poc.spring.blaze.fullapp.role.RoleService;
import de.kontextwork.poc.spring.blaze.fullapp.role.model.jpa.RealmRole;
import de.kontextwork.poc.spring.blaze.fullapp.role.model.jpa.Role;
import de.kontextwork.poc.spring.blaze.fullapp.role.model.view.RoleIdView;
import de.kontextwork.poc.spring.blaze.fullapp.rolemembership.RealmRoleMembershipService;
import de.kontextwork.poc.spring.blaze.fullapp.rolemembership.model.view.GlobalRoleMembershipCreateView;
import de.kontextwork.poc.spring.blaze.fullapp.rolemembership.model.view.RealmRoleMembershipCreateView;
import de.kontextwork.poc.spring.blaze.fullapp.subject.SubjectService;
import de.kontextwork.poc.spring.blaze.fullapp.subject.model.view.SubjectIdView;
import de.kontextwork.poc.spring.blaze.fullapp.subject.user.model.jpa.User;
import de.kontextwork.poc.spring.configuration.BlazePersistenceConfiguration;
import de.kontextwork.poc.spring.configuration.JpaBlazeConfiguration;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.AutoConfigureDataJdbc;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

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
class GeneralBlazePersistenceTests
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
  @DisplayName("Should collect CreateViews as Set")
  @Sql(
    statements = "alter table subject_user modify uid bigint auto_increment;",
    executionPhase = ExecutionPhase.BEFORE_TEST_METHOD
  )
  void shouldCollectCreateViewsAsSet()
  {
    User userJohn = subjectService.create(new User("John", "Doe"));
    User userJane = subjectService.create(new User("Jane", "Doe"));
    Realm realm = realmService.create(new Realm("Random"));
    RealmRole role = roleService.create(new RealmRole("SOME_ROLE", Set.of()));

    final Function<User, RealmRoleMembershipCreateView> createCreateViewForUser = usr -> {
      final SubjectIdView subjectIdView = subjectService.getOneAsIdView(usr.getId()).orElseThrow();
      final RealmIdView realmIdView = realmService.getOneAsIdView(realm.getId()).orElseThrow();
      final RoleIdView roleIdView = roleService.getOneAsIdView(role.getId()).orElseThrow();

      RealmRoleMembershipCreateView createView = entityViewManager.create(RealmRoleMembershipCreateView.class);
      createView.setRealm(realmIdView);
      createView.setRole(roleIdView);
      createView.setSubject(subjectIdView);

      return createView;
    };

    final Set<RealmRoleMembershipCreateView> setCollection = Stream.of(userJohn, userJane)
      .map(createCreateViewForUser)
      .collect(Collectors.toSet());

    final List<RealmRoleMembershipCreateView> listCollection = Stream.of(userJohn, userJane)
      .map(createCreateViewForUser)
      .collect(Collectors.toList());

    assertThat(setCollection).hasSize(2);
    assertThat(listCollection).hasSize(2);
  }

  @Test
  @DisplayName("Should create non equal CreateViews")
  @Sql(
    statements = "alter table subject_user modify uid bigint auto_increment;",
    executionPhase = ExecutionPhase.BEFORE_TEST_METHOD
  )
  void shouldCreateNonEqualCreateViews()
  {
    User userJohn = subjectService.create(new User("John", "Doe"));
    User userJane = subjectService.create(new User("Jane", "Doe"));
    Realm realm = realmService.create(new Realm("Random"));
    RealmRole role = roleService.create(new RealmRole("SOME_ROLE", Set.of()));

    final RealmIdView realmIdView = realmService.getOneAsIdView(realm.getId()).orElseThrow();
    final RoleIdView roleIdView = roleService.getOneAsIdView(role.getId()).orElseThrow();

    final SubjectIdView johnSubjectIdView = subjectService.getOneAsIdView(userJohn.getId()).orElseThrow();
    RealmRoleMembershipCreateView johnCreateView = entityViewManager.create(RealmRoleMembershipCreateView.class);
    johnCreateView.setRealm(realmIdView);
    johnCreateView.setRole(roleIdView);
    johnCreateView.setSubject(johnSubjectIdView);

    RealmRoleMembershipCreateView duplicateJohnCreateView =
      entityViewManager.create(RealmRoleMembershipCreateView.class);
    duplicateJohnCreateView.setRealm(realmIdView);
    duplicateJohnCreateView.setRole(roleIdView);
    duplicateJohnCreateView.setSubject(johnSubjectIdView);

    final SubjectIdView janeSubjectIdView = subjectService.getOneAsIdView(userJane.getId()).orElseThrow();
    RealmRoleMembershipCreateView janeCreateView = entityViewManager.create(RealmRoleMembershipCreateView.class);
    janeCreateView.setRealm(realmIdView);
    janeCreateView.setRole(roleIdView);
    janeCreateView.setSubject(janeSubjectIdView);

    assertThat(johnCreateView.equals(janeCreateView)).isFalse();
    assertThat(johnCreateView.equals(duplicateJohnCreateView)).isTrue();
  }
}
