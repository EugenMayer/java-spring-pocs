package de.kontextwork.poc.spring.many2many.bothnonpks.repository;

import de.kontextwork.poc.spring.many2many.bothnonpks.domain.ParentBothNonPk;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParentBothNonPkRepository extends
  JpaRepository<ParentBothNonPk, Long>
{
  Optional<ParentBothNonPk> findByParentId(Long parentId);
}
