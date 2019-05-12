package de.kontextwork.poc.spring.many2many.nonpkservice.repository;

import de.kontextwork.poc.spring.many2many.nonpkservice.domain.ParentNonPkServiceBased;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParentNonPkServiceBasedRepository extends
                                                   JpaRepository<ParentNonPkServiceBased, Long> {
  Optional<ParentNonPkServiceBased> findByParentId(Long parentId);
}
