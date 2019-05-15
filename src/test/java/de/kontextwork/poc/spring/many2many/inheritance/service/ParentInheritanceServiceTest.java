package de.kontextwork.poc.spring.many2many.inheritance.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.google.common.collect.Sets;
import de.kontextwork.poc.spring.many2many.inheritance.domain.ChildInheritanceBased;
import de.kontextwork.poc.spring.many2many.inheritance.domain.ParentInheritanceBased;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;

@DataJpaTest
@Import(ParentInheritanceService.class)
class ParentInheritanceServiceTest {
  @Autowired
  ParentInheritanceService parentInheritanceService;
  @Autowired
  JdbcTemplate jdbcTemplate;
  @Autowired
  EntityManager entityManager;

  @Test
  @DirtiesContext
  void getParent() {
    // one child presaved
    var child1 = new ChildInheritanceBased("child1");
    // one not presaved
    var child2 = new ChildInheritanceBased("child2");

    var parent1 = new ParentInheritanceBased("parent1");
    parent1.setChildren(Sets.newHashSet(child1, child2));
    parent1 = parentInheritanceService.save(parent1);

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

    var reloaded = parentInheritanceService.getParent(parent1.getId()).orElseThrow();
    assertEquals(2, reloaded.getChildren().size());
  }

  @Test
  @DirtiesContext
  void save() {
    // one child presaved
    var child1 = new ChildInheritanceBased("child1");
    // one not presaved
    var child2 = new ChildInheritanceBased("child2");

    var parent1 = new ParentInheritanceBased("parent1");
    parent1.setChildren(Sets.newHashSet(child1, child2));
    parent1 = parentInheritanceService.save(parent1);
    assertEquals(2, parent1.getChildren().size());


    // *** now try to remove a child again and see if that is reflected
    parent1.getChildren().removeIf(child -> child.getMachine().equals("child1"));
    parent1 = parentInheritanceService.save(parent1);
    assertEquals(1, parent1.getChildren().size());

    // enforce all the flush and reload and try to validate it once again, to rule out gliches
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
    var reloaded = parentInheritanceService.getParent(parent1.getId()).orElseThrow();
    assertEquals(1, reloaded.getChildren().size());

    // *** try to actually add the same child once again and ensure we get an exception
    assertTrue( reloaded.getChildren().stream().anyMatch(child -> child.getMachine().equals("child2")));
    reloaded.setChildren( Sets.newHashSet(new ChildInheritanceBased("child2")));
    Assertions.assertThrows(DataIntegrityViolationException.class, () -> parentInheritanceService.save(reloaded));
  }
}