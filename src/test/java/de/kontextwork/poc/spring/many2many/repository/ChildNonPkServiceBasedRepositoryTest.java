package de.kontextwork.poc.spring.many2many.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.google.common.collect.Sets;
import de.kontextwork.poc.spring.many2many.domain.nonpkservice.ChildNonPkServiceBased;
import de.kontextwork.poc.spring.many2many.domain.nonpkservice.ParentNonPkServiceBased;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.jdbc.core.JdbcTemplate;

@DataJpaTest
class ChildNonPkServiceBasedRepositoryTest {
  @Autowired
  ChildNonPkServiceBasedRepository childNonPkServiceBasedRepository;
  @Autowired
  ParentNonPkServiceBasedRepository parentNonPkServiceBasedRepository;
  @Autowired
  JdbcTemplate jdbcTemplate;
  @Autowired
  EntityManager entityManager;

  @Test
  void findAllByChildParentId() {
    // child1, yet not saved
    var child1 = new ChildNonPkServiceBased("child1");
    // child2, yet not saved
    var child2 = new ChildNonPkServiceBased("child2");

    var parent1 = new ParentNonPkServiceBased();
    parent1.setChildren(
        Sets.newHashSet(child1, child2)
    );
    // we flush since are going to use JDBC for the db checks
    parentNonPkServiceBasedRepository.saveAndFlush(parent1);

    assertEquals(2,
        childNonPkServiceBasedRepository.findAllByChildParentId(parent1.getParentId()).size());
  }
}