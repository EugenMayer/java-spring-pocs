package de.kontextwork.poc.spring.many2many.repository;

import de.kontextwork.poc.spring.many2many.domain.nonpk.ParentNonPkBased;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParentNonPkBasedRepository extends JpaRepository<ParentNonPkBased, Long> {
  Optional<ParentNonPkBased> findByParentId(Long parentId);
}
