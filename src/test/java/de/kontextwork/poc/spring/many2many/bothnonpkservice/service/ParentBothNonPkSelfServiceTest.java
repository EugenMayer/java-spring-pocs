package de.kontextwork.poc.spring.many2many.bothnonpkservice.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.google.common.collect.Sets;
import de.kontextwork.poc.spring.many2many.bothnonpkservice.domain.ChildBothNonPkServiceBased;
import de.kontextwork.poc.spring.many2many.bothnonpkservice.domain.ParentBothNonPkServiceBased;
import de.kontextwork.poc.spring.many2many.bothnonpkservice.repository.ChildBothNonPkServiceBasedRepository;
import de.kontextwork.poc.spring.many2many.bothnonpkservice.repository.ParentBothNonPkServiceBasedRepository;
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
@Import(ParentBothNonPkService.class)
class ParentBothNonPkSelfServiceTest {
  @Autowired
  ParentBothNonPkService parentBothNonPkService;
  @Autowired
  ParentBothNonPkServiceBasedRepository parentBothNonPkServiceBasedRepository;
  @Autowired
  ChildBothNonPkServiceBasedRepository childBothNonPkServiceBasedRepository;
  @Autowired
  JdbcTemplate jdbcTemplate;
  @Autowired
  EntityManager entityManager;

  @Test
  @DirtiesContext
  void getParent() {
    // first we create 3 children without adding them to a parent for ensuring our relation respects
    // only the one related to the parent
    childBothNonPkServiceBasedRepository.save(new ChildBothNonPkServiceBased("directSave1"));
    childBothNonPkServiceBasedRepository.save(new ChildBothNonPkServiceBased("directSave2"));
    childBothNonPkServiceBasedRepository.save(new ChildBothNonPkServiceBased("directSave3"));

    // child1, yet not saved
    var child1 = new ChildBothNonPkServiceBased("child1");
    // child2, yet not saved
    var child2 = new ChildBothNonPkServiceBased("child2");

    var parent1 = new ParentBothNonPkServiceBased("parent1");
    parent1.setChildren(
        Sets.newHashSet(child1, child2)
    );

    parentBothNonPkService.save(parent1);

    // This is also very interesting and important, if we would not use flush
    // loading using parentNonPkServiceBasedRepository.findById would actually even load children
    // even though it is not supposed to work. The reason for that is most probably and internal cache
    // which, if lost, cannot be rebuild out of the data (why so ever anyway). Since flush can happen
    // anytime, we have to ensure that his is included in our tests before we load to ensure we do not rely
    // on a "non reproduceable cache"
    entityManager.flush();

    // ensure that is working to, this can cause a special exception on it's own - but even refresh
    // will not have an garantees on children being "loadable" - is's more just a first level / low level
    // reload with the first level of "integration" of JPA required
    entityManager.refresh(parent1);

    entityManager.clear();

    // TODO: why did this suddenly start to work? Spring patch?
    // suddenly this started to work, making this whole strategy useless.
    // i am yet not sure what cause this
    assertEquals(
        2,
        parentBothNonPkServiceBasedRepository.findById(parent1.getParentId()).orElseThrow()
            .getChildren().size()
    );

    // using the service, it should have been populated now
    var reloaded = parentBothNonPkService.getParent(parent1.getParentId());
    assertEquals(2, reloaded.orElseThrow().getChildren().size());
  }

  @Test
  @DirtiesContext
  void saveParent() {
    // *** create a new parent with those 2 cihlds attached and ensure we get it loaded
    // one child presaved
    var child1 = new ChildBothNonPkServiceBased("child1");
    // one not presaved
    var child2 = new ChildBothNonPkServiceBased("child2");

    var parent1 = new ParentBothNonPkServiceBased("parent1");
    parent1.setChildren(Sets.newHashSet(child1, child2));
    parent1 = parentBothNonPkService.save(parent1);
    assertEquals(2, parent1.getChildren().size());

    // *** now try to remove a child again and see if that is reflected
    parent1.getChildren().removeIf(child -> child.getMachine().equals("child1"));
    parent1 = parentBothNonPkService.save(parent1);
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

    var reloaded = parentBothNonPkService.getParent(parent1.getParentId()).orElseThrow();
    assertEquals(1, reloaded.getChildren().size());

    // *** try to actually add the same child once again and ensure we get an exception
    assertTrue( reloaded.getChildren().stream().anyMatch(child -> child.getMachine().equals("child2")));
    reloaded.getChildren().add(new ChildBothNonPkServiceBased("child2"));
    Assertions.assertThrows(DataIntegrityViolationException.class, () -> parentBothNonPkService.save(reloaded));
  }
}