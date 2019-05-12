package de.kontextwork.poc.spring.many2many.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.google.common.collect.Sets;
import de.kontextwork.poc.spring.many2many.domain.nonpk.ChildNonPkBased;
import de.kontextwork.poc.spring.many2many.domain.nonpk.ParentNonPkBased;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;

@DataJpaTest()
@DirtiesContext
class ParentNonPkBasedRepositoryTest {
  @Autowired
  ParentNonPkBasedRepository parentNonPkBasedRepository;
  @Autowired
  JdbcTemplate jdbcTemplate;
  @Autowired
  EntityManager entityManager;

  @Test
  void createParentWithChildren() {
    // child1, yet not saved
    var child1 = new ChildNonPkBased("child1");
    // child2, yet not saved
    var child2 = new ChildNonPkBased("child2");

    var parent1 = new ParentNonPkBased();
    parent1.setChildren(
        Sets.newHashSet(child1, child2)
    );
    // we flush since are going to use JDBC for the db checks
    parentNonPkBasedRepository.saveAndFlush(parent1);

    // we should have 2 relations in here
    //noinspection SqlResolve
    List<Map<String, Object>> relationExists = jdbcTemplate
        .queryForList(
            "select * from join_table_parent_non_pk_based where myparent_id=?",
            parent1.getParentId()
        );
    assertEquals(2, relationExists.size());

    // we should have 2 children saved
    //noinspection SqlResolve
    List<Map<String, Object>> childsExist = jdbcTemplate
        .queryForList(
            "select * from child_non_pk_based"
        );
    assertEquals(2, childsExist.size());

    // this is important, we force the parent to be reread - this can already cause new
    // DDL based issues like de.kontextwork.poc.spring.many2many.domain.pk.ChildPkBased incompatible with java.io.Serializable
    // even though we could have written beforehand without any issues
    entityManager.refresh(parent1);

    // TODO: that is where things break - the loaded entity does not load its children
    var reloaded = parentNonPkBasedRepository.findByParentId(parent1.getParentId()).orElseThrow();
    assertEquals(2, reloaded.getChildren().size());
  }
}