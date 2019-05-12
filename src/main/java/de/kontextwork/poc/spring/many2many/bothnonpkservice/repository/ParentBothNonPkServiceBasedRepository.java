package de.kontextwork.poc.spring.many2many.bothnonpkservice.repository;

import de.kontextwork.poc.spring.many2many.bothnonpkservice.domain.ParentBothNonPkServiceBased;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParentBothNonPkServiceBasedRepository extends
                                                   JpaRepository<ParentBothNonPkServiceBased, Long> {
  Optional<ParentBothNonPkServiceBased> findByParentId(Long parentId);
}
