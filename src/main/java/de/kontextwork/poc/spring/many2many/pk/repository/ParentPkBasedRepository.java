package de.kontextwork.poc.spring.many2many.pk.repository;

import de.kontextwork.poc.spring.many2many.pk.domain.ParentPkBased;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParentPkBasedRepository extends JpaRepository<ParentPkBased, Long> {
  Optional<ParentPkBased> findByParentId(Long parentId);
  <T> List<T> findAllProjectedBy(Class<T> projection);
}
