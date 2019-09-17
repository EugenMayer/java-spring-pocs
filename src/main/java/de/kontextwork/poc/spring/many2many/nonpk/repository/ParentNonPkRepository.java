package de.kontextwork.poc.spring.many2many.nonpk.repository;

import de.kontextwork.poc.spring.many2many.nonpk.domain.ParentNonPk;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParentNonPkRepository extends JpaRepository<ParentNonPk, Long>
{
  Optional<ParentNonPk> findByParentId(Long parentId);
}
