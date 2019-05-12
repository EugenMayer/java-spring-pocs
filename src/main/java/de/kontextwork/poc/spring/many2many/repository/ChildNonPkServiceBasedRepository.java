package de.kontextwork.poc.spring.many2many.repository;

import de.kontextwork.poc.spring.many2many.domain.nonpkservice.ChildNonPkServiceBased;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ChildNonPkServiceBasedRepository extends JpaRepository<ChildNonPkServiceBased, Long> {
  Optional<ChildNonPkServiceBased> findByMachine(String machine);
  Optional<ChildNonPkServiceBased> findBySomeId(Long someId);
  // TODO: not sure how to make this a non-native query since we seem to no be able to address the inner join
  // https://www.baeldung.com/spring-data-jpa-query
  @Query(
      //value = "SELECT ChildNonPkServiceBased FROM ChildNonPkServiceBased INNER JOIN join_table_parent_non_pk_service_based AS jointable ON ChildNonPkServiceBased.machine=jointable.machine WHERE jointable.myparent_id = :parent_id"
      value = "SELECT child.* FROM child_non_pk_service_based AS child INNER JOIN join_table_parent_non_pk_service_based AS jointable ON child.machine=jointable.mychild_machine WHERE jointable.myparent_id = :parent_id",
      nativeQuery = true
  )
  Set<ChildNonPkServiceBased> findAllByChildParentId(@Param("parent_id") Long childParentId);
}
