package de.kontextwork.poc.spring.many2many.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.google.common.collect.Sets;
import de.kontextwork.poc.spring.many2many.domain.nonpkservice.ChildNonPkServiceBased;
import de.kontextwork.poc.spring.many2many.domain.nonpkservice.ParentNonPkServiceBased;
import de.kontextwork.poc.spring.many2many.repository.ChildNonPkServiceBasedRepository;
import de.kontextwork.poc.spring.many2many.repository.ParentNonPkServiceBasedRepository;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;

@DataJpaTest
@Import(ParentNonPkService.class)
class ParentNonPkServiceTest {
  @Autowired
  ParentNonPkService parentNonPkService;
  @Autowired
  ParentNonPkServiceBasedRepository parentNonPkServiceBasedRepository;
  @Autowired
  ChildNonPkServiceBasedRepository childNonPkServiceBasedRepository;
  @Autowired
  JdbcTemplate jdbcTemplate;
  @Autowired
  EntityManager entityManager;

  @Test
  @DirtiesContext
  void getParent() {
    // firt we create 3 childs without adding them to a parent for ensuring our relation respects
    // only the one related to the parent
    childNonPkServiceBasedRepository.save(new ChildNonPkServiceBased("directSave1"));
    childNonPkServiceBasedRepository.save(new ChildNonPkServiceBased("directSave2"));
    childNonPkServiceBasedRepository.save(new ChildNonPkServiceBased("directSave3"));

    // child1, yet not saved
    var child1 = new ChildNonPkServiceBased("child1");
    // child2, yet not saved
    var child2 = new ChildNonPkServiceBased("child2");

    var parent1 = new ParentNonPkServiceBased();
    parent1.setChildren(
        Sets.newHashSet(child1, child2)
    );

    parentNonPkService.save(parent1);

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

    // since jpa cannot load our non-pk based m2m relation, children are expected to be not populated
    assertEquals(
        0,
        parentNonPkServiceBasedRepository.findById(parent1.getParentId()).orElseThrow()
            .getChildren().size()
    );

    // using the service, it should have been populated now
    var reloaded = parentNonPkService.getParent(parent1.getParentId());
    assertEquals(2, reloaded.orElseThrow().getChildren().size());
  }

  @Test
  @DirtiesContext
  void saveParent() {
    // one child presaved
    var child1 = new ChildNonPkServiceBased("child1");
    // one not presaved
    var child2 = new ChildNonPkServiceBased("child2");

    var parent1 = new ParentNonPkServiceBased();
    parent1.setChildren(Sets.newHashSet(child1, child2));
    parent1 = parentNonPkService.save(parent1);
    assertEquals(2, parent1.getChildren().size());
  }
}