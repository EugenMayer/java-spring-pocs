package de.kontextwork.poc.spring.blaze.user;

import com.blazebit.persistence.view.EntityViewManager;
import com.blazebit.persistence.view.EntityViewSetting;
import de.kontextwork.poc.spring.blaze.user.model.domain.*;
import de.kontextwork.poc.spring.blaze.user.model.jpa.BlazeUser;
import de.kontextwork.poc.spring.configuration.BlazePersistenceConfiguration;
import de.kontextwork.poc.spring.configuration.JpaBlazeConfiguration;
import java.util.Random;
import java.util.UUID;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.AutoConfigureDataJdbc;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest(
  properties = {
    "blazepersistance.enabled=true"
  }
)
@AutoConfigureDataJdbc
@Import({
  //EntityViewManager.class,
  //CriteriaBuilderFactory.class,
  BlazePersistenceConfiguration.class,
  JpaBlazeConfiguration.class,
  BlazeService.class
})
class BlazeServiceTest
{
  @Autowired
  BlazeService blazeService;

  @Autowired
  EntityManager entityManager;

  @Autowired
  EntityViewManager entityViewManager;

  BlazeUser buildUser()
  {
    return (new BlazeUser()
      .setFirstName("firstName1")
      .setLastName("lastName1")
      .setUid(new Random().nextInt())
    );
  }

  @Test
  void createByView()
  {
    final BlazeUserCreateView blazeUserCreateView = entityViewManager.create(BlazeUserCreateView.class);
    blazeUserCreateView.setUserName(UUID.randomUUID().toString());
    blazeService.create(blazeUserCreateView);
  }

  @Test
  void getOne()
  {
    Integer expectedUid = 123;
    var blazeUser = blazeService.create(buildUser().setUid(expectedUid));

    // ensure we can flush and we do not load from L1
    entityManager.flush();
    entityManager.clear();

    // no assert, this is enough
    blazeService.getOne(
      blazeUser.getUid(),
      EntityViewSetting.create(BlazeUserIdView.class)
    ).orElseThrow();
  }

  @Test
  void updateUserProfile()
  {
    Integer expectedUid = 123;
    var blazeUser = blazeService.create(buildUser().setUid(expectedUid));

    // ensure we can flush and we do not load from L1
    entityManager.flush();
    entityManager.clear();

    // no assert, this is enough
    BlazeUserProfileUpdateView toBechanged = blazeService.getOne(
      blazeUser.getUid(),
      EntityViewSetting.create(BlazeUserProfileUpdateView.class)
    ).orElseThrow();

    toBechanged.setFirstName("changedFirstname");
    toBechanged.setLastName("changedLastname");

    blazeService.updateUserProfile(toBechanged);
  }
}