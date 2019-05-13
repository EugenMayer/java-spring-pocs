package de.kontextwork.poc.spring.many2many.inheritance.repository;

import de.kontextwork.poc.spring.many2many.bothnonpkservice.domain.ChildBothNonPkServiceBased;
import de.kontextwork.poc.spring.many2many.inheritance.Domain.ChildInheritanceBased;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ChildInheritanceBasedRepository extends
                                                  JpaRepository<ChildInheritanceBased, Long> {
  Optional<ChildInheritanceBased> findByMachine(String machine);
  Optional<ChildInheritanceBased> findById(Long elementId);
  @Query(
      //value = "SELECT ChildNonPkServiceBased FROM ChildNonPkServiceBased INNER JOIN join_table_parent_non_pk_service_based AS jointable ON ChildNonPkServiceBased.machine=jointable.machine WHERE jointable.myparent_id = :parent_id"
      value = "SELECT child.* FROM inheritance_base_type AS child INNER JOIN join_table_inheritance AS jointable ON child.machine=jointable.mychild_machine WHERE jointable.myparent_machine = :parent_machine",
      nativeQuery = true
  )
  Set<ChildInheritanceBased> findAllByChildParentMachine(@Param("parent_machine") String childParentMachine);
}
