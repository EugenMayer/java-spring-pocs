package de.kontextwork.poc.spring.many2many.repository;

import de.kontextwork.poc.spring.many2many.domain.pk.ParentPkBased;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParentPkBasedRepository extends JpaRepository<ParentPkBased, Long> {
  Optional<ParentPkBased> findByParentId(Long parentId);
}
