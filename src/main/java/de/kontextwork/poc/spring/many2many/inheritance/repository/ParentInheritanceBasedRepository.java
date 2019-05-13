package de.kontextwork.poc.spring.many2many.inheritance.repository;

import de.kontextwork.poc.spring.many2many.inheritance.Domain.ParentInheritanceBased;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParentInheritanceBasedRepository extends
                                                   JpaRepository<ParentInheritanceBased, Long> {
  Optional<ParentInheritanceBased> findById(Long id);
}
