package de.kontextwork.poc.spring.many2many.bothnonpkservice.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.google.common.collect.Sets;
import de.kontextwork.poc.spring.many2many.bothnonpkservice.domain.ChildBothNonPkServiceBased;
import de.kontextwork.poc.spring.many2many.bothnonpkservice.domain.ParentBothNonPkServiceBased;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;

@DataJpaTest
class ChildInheritanceBasedRepositoryTest {
  @Autowired
  ChildBothNonPkServiceBasedRepository childBothNonPkServiceBasedRepository;
  @Autowired
  ParentBothNonPkServiceBasedRepository parentBothNonPkServiceBasedRepository;
  @Autowired
  JdbcTemplate jdbcTemplate;
  @Autowired
  EntityManager entityManager;

  @Test
  @DirtiesContext
  void findAllByChildParentMachine() {
    // child1, yet not saved
    var child1 = new ChildBothNonPkServiceBased("child1");
    // child2, yet not saved
    var child2 = new ChildBothNonPkServiceBased("child2");

    var parent1 = new ParentBothNonPkServiceBased("parent1");
    parent1.setChildren(
        Sets.newHashSet(child1, child2)
    );
    // we flush since are going to use JDBC for the db checks
    parentBothNonPkServiceBasedRepository.saveAndFlush(parent1);

    assertEquals(
        2,
        childBothNonPkServiceBasedRepository.findAllByChildParentMachine(parent1.getMachine()).size()
    );
  }
}