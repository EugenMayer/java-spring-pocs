package de.kontextwork.poc.spring.many2many.repository;

import de.kontextwork.poc.spring.many2many.domain.naturalid.ParentNaturalIdBased;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParentNaturalIdBasedRepository extends JpaRepository<ParentNaturalIdBased, Long> {
  Optional<ParentNaturalIdBased> findByParentId(Long parentId);
}
