package de.kontextwork.poc.spring.many2many.pk.repository;

import com.google.common.collect.Sets;
import de.kontextwork.poc.spring.configuration.BlazePersistenceConfiguration;
import de.kontextwork.poc.spring.configuration.HibernateConfiguration;
import de.kontextwork.poc.spring.many2many.pk.domain.ChildPkBased;
import de.kontextwork.poc.spring.many2many.pk.domain.ParentPkBased;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import static junit.framework.TestCase.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@Import(
  {
    HibernateConfiguration.class,
    BlazePersistenceConfiguration.class
  }
)
class ParentPkBasedRepositoryTest
{
  @Autowired
  ParentPkBasedRepository parentPkBasedRepository;
  @Autowired
  JdbcTemplate jdbcTemplate;
  @Autowired
  EntityManager entityManager;

  @Test
  @Transactional
  void crudParentWithChildren()
  {
    var child1 = new ChildPkBased("child1");
    var child2 = new ChildPkBased("child2");

    var parent1 = new ParentPkBased();
    parent1.setChildren(
      Sets.newHashSet(child1, child2)
    );

    parentPkBasedRepository.saveAndFlush(parent1);
    // we should have 2 relations in here
    List<Map<String, Object>> relationExists = jdbcTemplate
      .queryForList(
        "select * from join_table_parent_pk_based where myparent_id=?",
        parent1.getParentId()
      );
    assertEquals(2, relationExists.size());

    // we should have 2 children saved
    List<Map<String, Object>> childsExist = jdbcTemplate
      .queryForList(
        "select * from child_pk_based"
      );
    assertEquals(2, childsExist.size());

    // this is important, we force the parent to be reread - this can already cause new
    // DDL based issues like de.kontextwork.poc.spring.many2many.pk.domain.ChildPkBased incompatible with java.io
    // .Serializable
    // even though we could have written beforehand without any issues
    entityManager.refresh(parent1);

    var reloaded = parentPkBasedRepository.findByParentId(parent1.getParentId()).orElseThrow();
    assertEquals(2, reloaded.getChildren().size());

    // *** test delete operations
    reloaded.getChildren().removeIf(child -> child.getName().equals("child1"));
    parentPkBasedRepository.save(reloaded);

    entityManager.flush();
    entityManager.refresh(reloaded);
    entityManager.clear();

    List<Map<String, Object>> deletedOnRelation = jdbcTemplate
      .queryForList(
        "select * from join_table_parent_pk_based where myparent_id=?",
        parent1.getParentId()
      );
    assertEquals(1, deletedOnRelation.size());

    reloaded = parentPkBasedRepository.findById(parent1.getParentId()).orElseThrow();
    assertEquals(1, reloaded.getChildren().size());

    // ** delete the parent
    parentPkBasedRepository.delete(reloaded);

    entityManager.flush();
    entityManager.clear();

    List<Map<String, Object>> deletedAll = jdbcTemplate
      .queryForList(
        "select * from join_table_parent_pk_based where myparent_id=?",
        parent1.getParentId()
      );
    assertEquals(0, deletedAll.size());
    assertTrue(parentPkBasedRepository.findById(parent1.getParentId()).isEmpty());
  }
}