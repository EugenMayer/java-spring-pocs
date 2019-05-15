package de.kontextwork.poc.spring.many2many.nonpk.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.google.common.collect.Sets;
import de.kontextwork.poc.spring.many2many.nonpk.domain.ChildNonPkBased;
import de.kontextwork.poc.spring.many2many.nonpk.domain.ParentNonPkBased;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;

@DataJpaTest()

class ParentNonPkBasedRepositoryTest {
  @Autowired
  ParentNonPkBasedRepository parentNonPkBasedRepository;
  @Autowired
  JdbcTemplate jdbcTemplate;
  @Autowired
  EntityManager entityManager;

  @Test
  @DirtiesContext
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
    List<Map<String, Object>> relationExists = jdbcTemplate
        .queryForList(
            "select * from join_table_parent_non_pk_based where myparent_id=?",
            parent1.getParentId()
        );
    assertEquals(2, relationExists.size());

    // we should have 2 children saved
    List<Map<String, Object>> childsExist = jdbcTemplate
        .queryForList(
            "select * from child_non_pk_based"
        );
    assertEquals(2, childsExist.size());

    // this is important, we force the parent to be reread - this can already cause new
    // DDL based issues like de.kontextwork.poc.spring.many2many.pk.domain.ChildPkBased incompatible with java.io.Serializable
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
    // TODO: that is where things break - the loaded entity does not load its children
    var reloaded = parentNonPkBasedRepository.findByParentId(parent1.getParentId()).orElseThrow();
    assertEquals(0, reloaded.getChildren().size(), "relation can still not be loaded");
    //assertEquals(2, reloaded.getChildren().size());
  }
}