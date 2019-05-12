package de.kontextwork.poc.spring.many2many.repository;

import de.kontextwork.poc.spring.many2many.domain.nonpkservice.ParentNonPkServiceBased;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParentNonPkServiceBasedRepository extends
                                                   JpaRepository<ParentNonPkServiceBased, Long> {
  Optional<ParentNonPkServiceBased> findByParentId(Long parentId);
}
