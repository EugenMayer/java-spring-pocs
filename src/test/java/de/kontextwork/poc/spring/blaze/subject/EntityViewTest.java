package de.kontextwork.poc.spring.blaze.subject;

import com.blazebit.persistence.view.EntityViewManager;
import de.kontextwork.poc.spring.blaze.core.RegularEntityViewRepository;
import de.kontextwork.poc.spring.blaze.subject.model.domain.UserCreateView;
import de.kontextwork.poc.spring.blaze.user.EntityViewService;
import de.kontextwork.poc.spring.configuration.BlazePersistenceConfiguration;
import de.kontextwork.poc.spring.configuration.JpaBlazeConfiguration;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.AutoConfigureDataJdbc;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Import({
  JpaBlazeConfiguration.class,
  BlazePersistenceConfiguration.class,
  RegularEntityViewRepository.class,
  EntityViewService.class
})
@AutoConfigureDataJdbc
@DataJpaTest(properties = {"blazepersistance.enabled=true"})
public class EntityViewTest
{
  @Autowired
  private EntityViewManager entityViewManager;

  @Autowired
  private EntityViewService entityViewService;

  @Test
  @DisplayName("Should create User via entity view")
  @Sql(
    statements = "alter table subject_user modify uid bigint auto_increment;",
    executionPhase = ExecutionPhase.BEFORE_TEST_METHOD
  )
  public void shouldCreateUserViaEntityView()
  {
    final UserCreateView userCreateView = entityViewManager.create(UserCreateView.class);
    userCreateView.setFirstName("Max");
    userCreateView.setLastName("Muster");
    entityViewService.createUser(userCreateView);

    assertThat(userCreateView.getId()).isGreaterThan(0);
  }
}
