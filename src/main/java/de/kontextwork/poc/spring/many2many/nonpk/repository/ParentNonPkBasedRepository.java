package de.kontextwork.poc.spring.many2many.nonpk.repository;

import de.kontextwork.poc.spring.many2many.nonpk.domain.ParentNonPkBased;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParentNonPkBasedRepository extends JpaRepository<ParentNonPkBased, Long> {
  Optional<ParentNonPkBased> findByParentId(Long parentId);
}
