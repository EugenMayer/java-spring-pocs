package de.kontextwork.poc.spring.blaze.fullapp.rolemembership;

import com.blazebit.persistence.view.EntityViewSetting;
import de.kontextwork.poc.spring.blaze.core.*;
import de.kontextwork.poc.spring.blaze.fullapp.realm.RealmService;
import de.kontextwork.poc.spring.blaze.fullapp.realm.model.jpa.Realm;
import de.kontextwork.poc.spring.blaze.fullapp.realm.model.view.RealmIdView;
import de.kontextwork.poc.spring.blaze.fullapp.role.RoleService;
import de.kontextwork.poc.spring.blaze.fullapp.role.model.jpa.GlobalRole;
import de.kontextwork.poc.spring.blaze.fullapp.role.model.view.RoleIdView;
import de.kontextwork.poc.spring.blaze.fullapp.rolemembership.model.view.*;
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
  RoleService.class,
  GlobalRoleMembershipService.class,
  JpaBlazeConfiguration.class,
  BlazePersistenceConfiguration.class,
  PageableEntityViewRepository.class,
  RegularEntityViewRepository.class,
  ModelMapper.class
})
@AutoConfigureDataJdbc
@DataJpaTest(properties = {"blazepersistance.enabled=true"})
class GlobalRoleMembershipServiceTest
{

  @Autowired
  private SubjectService subjectService;

  @Autowired
  private RoleService roleService;

  @Autowired
  private GlobalRoleMembershipService globalRoleMembershipService;

  @Test
  @Sql(
    statements = "alter table subject_user modify uid bigint auto_increment;",
    executionPhase = ExecutionPhase.BEFORE_TEST_METHOD
  )
  void assign()
  {
    User user = subjectService.create(new User("John", "Doe"));
    GlobalRole role = roleService.create(new GlobalRole("SOME_ROLE", Set.of()));
    globalRoleMembershipService.assign(role,user);
    assertThat( globalRoleMembershipService.findAll(EntityViewSetting.create(GlobalRoleMembershipIdView.class)).size() ).isNotZero();
  }
}