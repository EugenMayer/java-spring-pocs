package de.kontextwork.poc.spring.many2many.inheritance.repository;

import com.google.common.collect.Sets;
import de.kontextwork.poc.spring.configuration.BlazePersistenceConfiguration;
import de.kontextwork.poc.spring.configuration.JpaConfiguration;
import de.kontextwork.poc.spring.many2many.inheritance.domain.ChildInheritanceBased;
import de.kontextwork.poc.spring.many2many.inheritance.domain.ParentInheritanceBased;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@Import(
  {
    JpaConfiguration.class,
    BlazePersistenceConfiguration.class
  }
)
class ParentInheritanceBasedRepositoryTest
{
  @Autowired
  ParentInheritanceBasedRepository parentInheritanceBasedRepository;
  @Autowired
  JdbcTemplate jdbcTemplate;
  @Autowired
  EntityManager entityManager;

  @Test
  @DirtiesContext
  void crudParentWithChildren()
  {
    var child1 = new ChildInheritanceBased("child1");
    var child2 = new ChildInheritanceBased("child2");

    var parent1 = new ParentInheritanceBased("parent1");
    parent1.setChildren(Sets.newHashSet(child1, child2));
    parent1 = parentInheritanceBasedRepository.save(parent1);

    entityManager.flush();
    entityManager.refresh(parent1);

    // see http://www.kubrynski.com/2017/04/understanding-jpa-l1-caching.html
    // we do clear to simulate production use of fresh entities
    // otherwise EM will cache our relations end e.g. reloading a entity from the database
    // will already have the relation populated eventhough it should not be, since we did not
    // inject them yet and have no joinTable relation - this is due the entityCache. Since in production
    // we can have cold cache, all our code must be cold cache proove, thus clear it before doing asserts
    // this is basically the "flush the read cache"
    entityManager.clear();

    // we should have 2 relations in here
    List<Map<String, Object>> relationExists = jdbcTemplate
      .queryForList(
        "select * from join_table_inheritance where myparent_machine=?",
        parent1.getMachine()
      );
    assertEquals(2, relationExists.size());

    var reloaded = parentInheritanceBasedRepository.findById(parent1.getId()).orElseThrow();
    assertEquals(2, reloaded.getChildren().size());

    // *** test delete operations
    reloaded.getChildren().removeIf(child -> child.getMachine().equals("child1"));
    parentInheritanceBasedRepository.save(reloaded);

    entityManager.flush();
    entityManager.refresh(reloaded);
    entityManager.clear();

    List<Map<String, Object>> deletedOnRelation = jdbcTemplate
      .queryForList(
        "select * from join_table_inheritance where myparent_machine=?",
        parent1.getMachine()
      );
    assertEquals(1, deletedOnRelation.size());

    reloaded = parentInheritanceBasedRepository.findById(parent1.getId()).orElseThrow();
    assertEquals(1, reloaded.getChildren().size());

    // ** delete the parent now
    parentInheritanceBasedRepository.delete(reloaded);
    entityManager.flush();
    entityManager.clear();

    List<Map<String, Object>> deletedAll = jdbcTemplate
      .queryForList(
        "select * from join_table_inheritance where myparent_machine=?",
        parent1.getMachine()
      );
    assertEquals(0, deletedAll.size());
    assertTrue(parentInheritanceBasedRepository.findById(parent1.getId()).isEmpty());
  }
}