package de.kontextwork.poc.spring.many2many.bothnonpkservice.repository;

import de.kontextwork.poc.spring.many2many.bothnonpkservice.domain.ChildBothNonPkServiceBased;
import de.kontextwork.poc.spring.many2many.nonpkservice.domain.ChildNonPkServiceBased;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ChildBothNonPkServiceBasedRepository extends
                                                  JpaRepository<ChildBothNonPkServiceBased, Long> {
  Optional<ChildNonPkServiceBased> findByMachine(String machine);

  Optional<ChildNonPkServiceBased> findBySomeId(Long someId);

  // TODO: not sure how to make this a non-native query since we seem to no be able to address the inner join
  // https://www.baeldung.com/spring-data-jpa-query
  @Query(
      //value = "SELECT ChildNonPkServiceBased FROM ChildNonPkServiceBased INNER JOIN join_table_parent_non_pk_service_based AS jointable ON ChildNonPkServiceBased.machine=jointable.machine WHERE jointable.myparent_id = :parent_id"
      value = "SELECT child.* FROM child_both_non_pk_service_based AS child INNER JOIN join_table_parent_both_non_pk AS jointable ON child.machine=jointable.mychild_machine WHERE jointable.myparent_machine = :parent_machine",
      nativeQuery = true
  )
  Set<ChildBothNonPkServiceBased> findAllByChildParentMachine(@Param("parent_machine") String childParentMachine);
}
