package de.kontextwork.poc.spring.many2many.bothnonpks.repository;

import com.google.common.collect.Sets;
import de.kontextwork.poc.spring.configuration.BlazePersistenceConfiguration;
import de.kontextwork.poc.spring.configuration.HibernateConfiguration;
import de.kontextwork.poc.spring.many2many.bothnonpks.domain.ChildBothNonPk;
import de.kontextwork.poc.spring.many2many.bothnonpks.domain.ParentBothNonPk;
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
    HibernateConfiguration.class,
    BlazePersistenceConfiguration.class
  }
)
class ParentBothNonPkRepositoryTest
{
  @Autowired
  ParentBothNonPkRepository parentBothNonPkRepository;
  @Autowired
  JdbcTemplate jdbcTemplate;
  @Autowired
  EntityManager entityManager;

  @Test
  @DirtiesContext
  void crudParentWithChildren()
  {
    var child1 = new ChildBothNonPk("child1");
    var child2 = new ChildBothNonPk("child2");

    var parent1 = new ParentBothNonPk("parent1");
    parent1.setChildren(
      Sets.newHashSet(child1, child2)
    );
    // we flush since are going to use JDBC for the db checks
    parentBothNonPkRepository.saveAndFlush(parent1);

    // we should have 2 relations in here
    List<Map<String, Object>> relationExists = jdbcTemplate
      .queryForList(
        "select * from join_table_parent_both_non_pk where myparent_machine=?",
        parent1.getMachine()
      );
    assertEquals(2, relationExists.size());

    // we should have 2 children saved
    List<Map<String, Object>> childrenExist = jdbcTemplate
      .queryForList(
        "select * from child_both_non_pk"
      );
    assertEquals(2, childrenExist.size());

    // this is important, we force the parent to be reread - this can already cause new
    // DDL based issues like de.kontextwork.poc.spring.many2many.pk.domain.ChildPkBased incompatible with java.io
    // .Serializable
    // even though we could have written beforehand without any issues
    entityManager.refresh(parent1);
    entityManager.flush();
    // see http://www.kubrynski.com/2017/04/understanding-jpa-l1-caching.html
    // we do clear to simulate production use of fresh entities
    // otherwise EM will cache our relations end e.g. reloading a entity from the database
    // will already have the relation populated eventhough it should not be, since we did not
    // inject them yet and have no joinTable relation - this is due the entityCache. Since in production
    // we can have cold cache, all our code must be cold cache proove, thus clear it before doing asserts
    // this is basically the "flush the read cache"
    entityManager.clear();
    var reloaded = parentBothNonPkRepository.findByParentId(parent1.getParentId()).orElseThrow();
    assertEquals(2, reloaded.getChildren().size());

    // *** test delete operations
    reloaded.getChildren().removeIf(child -> child.getMachine().equals("child1"));
    parentBothNonPkRepository.save(reloaded);

    entityManager.flush();
    entityManager.refresh(reloaded);
    entityManager.clear();

    List<Map<String, Object>> deletedOnRelation = jdbcTemplate
      .queryForList(
        "select * from join_table_parent_both_non_pk where myparent_machine=?",
        parent1.getMachine()
      );
    assertEquals(1, deletedOnRelation.size());

    reloaded = parentBothNonPkRepository.findById(parent1.getParentId()).orElseThrow();
    assertEquals(1, reloaded.getChildren().size());

    // ** delete the parent
    parentBothNonPkRepository.delete(reloaded);

    entityManager.flush();
    entityManager.clear();

    List<Map<String, Object>> deletedAll = jdbcTemplate
      .queryForList(
        "select * from join_table_parent_both_non_pk where myparent_machine=?",
        parent1.getMachine()
      );
    assertEquals(0, deletedAll.size());
    assertTrue(parentBothNonPkRepository.findById(parent1.getParentId()).isEmpty());
  }
}