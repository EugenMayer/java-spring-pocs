package de.kontextwork.poc.spring.many2many.inheritance.repository;

import de.kontextwork.poc.spring.many2many.inheritance.domain.ChildParentAssociation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChildAssociationRepository extends
                                                  JpaRepository<ChildParentAssociation, Long> {
  void deleteById_ParentMachine(String parentMachine);
}
