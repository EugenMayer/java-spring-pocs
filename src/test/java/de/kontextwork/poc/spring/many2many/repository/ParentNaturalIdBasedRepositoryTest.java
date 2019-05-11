package de.kontextwork.poc.spring.many2many.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.google.common.collect.Sets;
import de.kontextwork.poc.spring.many2many.domain.naturalid.ChildNaturalId;
import de.kontextwork.poc.spring.many2many.domain.naturalid.ParentNaturalIdBased;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.jdbc.core.JdbcTemplate;

@DataJpaTest
class ParentNaturalIdBasedRepositoryTest {
    @Autowired
    ParentNaturalIdBasedRepository parentNaturalIdBasedRepository;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    EntityManager entityManager;

    @Test
    void createParentWithChildren() {
        // one child presaved
        var child1 = new ChildNaturalId("child1");
        // one not presaved
        var child2notSaved = new ChildNaturalId("child2");

        var parent1 = new ParentNaturalIdBased();
        parent1.setChildren(
            Sets.newHashSet(child1, child2notSaved)
        );
        // we flush since are going to use JDBC for the db checks
        parentNaturalIdBasedRepository.saveAndFlush(parent1);

        // we should have 2 relations in here
        //noinspection SqlResolve
        List<Map<String, Object>> relationExists = jdbcTemplate
            .queryForList(
                "select * from join_table_parent_naturalid_based where myparent_id=?",
                parent1.getParentId()
            );
        assertEquals(2, relationExists.size());

        // we should have 2 children saved
        //noinspection SqlResolve
        List<Map<String, Object>> childsExist = jdbcTemplate
            .queryForList(
                "select * from child_natural_id"
            );
        assertEquals(2, childsExist.size());

        // this is important, we force the parent to be reread - this can already cause new
        // DDL based issues like de.kontextwork.poc.spring.many2many.domain.pk.ChildPk incompatible with java.io.Serializable
        // even though we could have written beforehand without any issues
        entityManager.refresh(parent1);

        // TODO: that is where things break - the loaded entity does not load its children
        var reloaded = parentNaturalIdBasedRepository.findByParentId(parent1.getParentId()).orElseThrow();
        assertEquals(2, reloaded.getChildren().size());
    }
}