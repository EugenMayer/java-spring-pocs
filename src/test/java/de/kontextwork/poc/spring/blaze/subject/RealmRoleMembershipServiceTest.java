package de.kontextwork.poc.spring.blaze.subject;

import com.blazebit.persistence.view.EntityViewSetting;
import com.github.javafaker.Faker;
import de.kontextwork.poc.spring.blaze.core.*;
import de.kontextwork.poc.spring.blaze.subject.model.domain.*;
import de.kontextwork.poc.spring.blaze.subject.model.jpa.User;
import de.kontextwork.poc.spring.blaze.subject.model.jpa.privilege.GlobalPrivilege;
import de.kontextwork.poc.spring.blaze.subject.model.jpa.privilege.RealmPrivilege;
import de.kontextwork.poc.spring.blaze.subject.model.jpa.role.*;
import de.kontextwork.poc.spring.blaze.subject.model.jpa.subject.Group;
import de.kontextwork.poc.spring.blaze.subject.model.jpa.subject.Realm;
import de.kontextwork.poc.spring.configuration.BlazePersistenceConfiguration;
import de.kontextwork.poc.spring.configuration.JpaBlazeConfiguration;
import java.util.Random;
import java.util.Set;
import java.util.function.IntFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
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
  private RealmRoleMembershipService realmRoleMembershipService;

  @Test
  @DisplayName("Should create RealmRoleMembership via EntityView")
  @Sql(
    statements = "alter table subject_user modify uid bigint auto_increment;",
    executionPhase = ExecutionPhase.BEFORE_TEST_METHOD
  )
  void shouldCreateRealmRoleMembershipViaEntityView()
  {
    User user = subjectService.create(new User("John", "Doe"));
    Realm realm = realmService.create(new Realm("Random"));
    GlobalRole role = roleService.create(new GlobalRole("SOME_ROLE", Set.of()));

    RealmRoleMembershipEmbeddedIdView idView = RealmRoleMembershipEmbeddedIdViewDTO.builder()
      .realmId(realm.getId())
      .roleId(role.getId())
      .subjectId(user.getId())
      .build();

    RealmRoleMembershipCreateView createView = RealmRoleMembershipCreateViewDTO.builder()
      .id(idView)
      .build();

    realmRoleMembershipService.create(createView);
  }
}