package de.kontextwork.poc.spring.many2many.service;

import static org.junit.jupiter.api.Assertions.*;

import com.google.common.collect.Sets;
import de.kontextwork.poc.spring.many2many.domain.nonpkservice.ChildNonPkServiceBased;
import de.kontextwork.poc.spring.many2many.domain.nonpkservice.ParentNonPkServiceBased;
import de.kontextwork.poc.spring.many2many.repository.ParentNonPkServiceBasedRepository;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;

@DataJpaTest
@Import(ParentNonPkService.class)
class ParentNonPkServiceTest {
  @Autowired
  ParentNonPkService parentNonPkService;

  @Autowired
  ParentNonPkServiceBasedRepository parentNonPkServiceBasedRepository;
  @Autowired
  JdbcTemplate jdbcTemplate;

  @Autowired
  EntityManager entityManager;

  @Test
  void getParent() {
    // one child presaved
    var child1 = new ChildNonPkServiceBased("child1");
    // one not presaved
    var child2notSaved = new ChildNonPkServiceBased("child2");

    var parent1 = new ParentNonPkServiceBased();
    parent1.setChildren(
        Sets.newHashSet(child1, child2notSaved)
    );
    parentNonPkService.save(parent1);

    // we flush since are going to use JDBC for the db checks
    entityManager.flush();

    // ensure that is working
    entityManager.refresh(parent1);

    // since jpa cannot load our non-pk based m2m relation, this will not be populated
    assertEquals(
        0,
        parentNonPkServiceBasedRepository.findById(parent1.getParentId()).orElseThrow().getChildren().size()
    );

    // using the service, it should have been populated now
    var reloaded = parentNonPkService.getParent(parent1.getParentId());
    assertEquals(2, reloaded.orElseThrow().getChildren().size());
  }


  @Test
  void saveParent() {
    // one child presaved
    var child1 = new ChildNonPkServiceBased("child1");
    // one not presaved
    var child2notSaved = new ChildNonPkServiceBased("child2");

    var parent1 = new ParentNonPkServiceBased();
    parent1.setChildren(
        Sets.newHashSet(child1, child2notSaved)
    );
    parentNonPkService.save(parent1);
  }
}