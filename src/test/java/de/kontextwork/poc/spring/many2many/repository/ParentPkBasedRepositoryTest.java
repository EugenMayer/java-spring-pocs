package de.kontextwork.poc.spring.many2many.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.google.common.collect.Sets;
import de.kontextwork.poc.spring.many2many.domain.pk.ChildPkBased;
import de.kontextwork.poc.spring.many2many.domain.pk.ParentPkBased;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.jdbc.core.JdbcTemplate;

@DataJpaTest
class ParentPkBasedRepositoryTest {
  @Autowired
  ParentPkBasedRepository parentPkBasedRepository;
  @Autowired
  JdbcTemplate jdbcTemplate;
  @Autowired
  EntityManager entityManager;

  @Test
  void createParentWithChildren() {
    // child1, yet not saved
    var child1 = new ChildPkBased("child1");
    // child2, yet not saved
    var child2 = new ChildPkBased("child2");

    var parent1 = new ParentPkBased();
    parent1.setChildren(
        Sets.newHashSet(child1, child2)
    );

    parentPkBasedRepository.saveAndFlush(parent1);
    // we should have 2 relations in here
    //noinspection SqlResolve
    List<Map<String, Object>> relationExists = jdbcTemplate
        .queryForList(
            "select * from join_table_parent_pk_based where myparent_id=?",
            parent1.getParentId()
        );
    assertEquals(2, relationExists.size());

    // we should have 2 children saved
    //noinspection SqlResolve
    List<Map<String, Object>> childsExist = jdbcTemplate
        .queryForList(
            "select * from child_pk_based"
        );
    assertEquals(2, childsExist.size());

    // this is important, we force the parent to be reread - this can already cause new
    // DDL based issues like de.kontextwork.poc.spring.many2many.domain.pk.ChildPkBased incompatible with java.io.Serializable
    // even though we could have written beforehand without any issues
    entityManager.refresh(parent1);

    var reloaded = parentPkBasedRepository.findByParentId(parent1.getParentId()).orElseThrow();
    assertEquals(2, reloaded.getChildren().size());
  }
}