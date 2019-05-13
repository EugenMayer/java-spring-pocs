package de.kontextwork.poc.spring.many2many.inheritance.repository;

import de.kontextwork.poc.spring.many2many.inheritance.Domain.ChildParentAssociation;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChildAssociationRepository extends
                                                  JpaRepository<ChildParentAssociation, Long> {
  Set<ChildParentAssociation> findById_ChildMachine(String childMachine);
  Set<ChildParentAssociation> findById_ParentMachine(String parentMachine);
  void deleteById_ParentMachine(String parentMachine);
}
