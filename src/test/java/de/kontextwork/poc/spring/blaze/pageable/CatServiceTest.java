package de.kontextwork.poc.spring.blaze.pageable;

import com.github.javafaker.Faker;
import de.kontextwork.poc.spring.blaze.pageable.model.domain.CatExcerptView;
import de.kontextwork.poc.spring.blaze.pageable.model.jpa.Cat;
import de.kontextwork.poc.spring.configuration.BlazePersistenceConfiguration;
import de.kontextwork.poc.spring.configuration.JpaBlazeConfiguration;
import java.util.List;
import java.util.function.IntFunction;
import java.util.stream.*;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.AutoConfigureDataJdbc;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.*;
import org.springframework.data.domain.Sort.Direction;

import static org.assertj.core.api.Assertions.assertThat;

@Import({
  CatService.class,
  JpaBlazeConfiguration.class,
  BlazePersistenceConfiguration.class,
})
@AutoConfigureDataJdbc
@DataJpaTest(properties = {"blazepersistance.enabled=true"})
class CatServiceTest
{
  private final Faker faker = new Faker();

  private final IntFunction<Cat> randomCat = id -> (new Cat())
    .setId((long) id)
    .setName(faker.cat().name())
    .setImplantCode(faker.random().hex(15));

  @Autowired
  private CatService catService;

  @Autowired
  private EntityManager entityManager;

  @Test
  @DisplayName("Should create 100 cats")
  void shouldCreateOneHundredCats()
  {
    List<Cat> cats = IntStream.rangeClosed(1, 100)
      .mapToObj(randomCat)
      .map(catService::create)
      .collect(Collectors.toList());

    catService.create(cats);

    assertThat(cats).hasSize(100);

    cats.forEach(cat -> {
      assertThat(cat.getName()).isNotBlank();
      assertThat(cat.getImplantCode()).hasSize(15);
    });
  }

  @Test
  @DisplayName("Should resolve Page of Cats")
  void shouldResolvePageOfCats()
  {
    shouldCreateOneHundredCats();

    // Force data into hibernation
    entityManager.flush();
    entityManager.clear();

    // PageRequest for second page with 25 cats
    PageRequest pageRequest = PageRequest.of(1, 25, Direction.DESC, "id", "name");

    // Fetch paged results the SpringData way
    Page<Cat> catsSpringDataPage = catService.read(pageRequest);

    // Fetch paged results the BlazePersistence way
    Page<CatExcerptView> catsBlazePersistencePage = catService.readExcerpt(pageRequest);

    assertThat(catsSpringDataPage).hasSameSizeAs(catsBlazePersistencePage);
    assertThat(catsSpringDataPage.getTotalPages()).isEqualTo(catsBlazePersistencePage.getTotalPages());
    assertThat(catsSpringDataPage.getTotalElements()).isEqualTo(catsBlazePersistencePage.getTotalElements());
    assertThat(catsSpringDataPage.getNumber()).isEqualTo(catsBlazePersistencePage.getNumber());
  }
}