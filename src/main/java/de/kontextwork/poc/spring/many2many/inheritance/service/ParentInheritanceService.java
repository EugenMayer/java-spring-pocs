package de.kontextwork.poc.spring.many2many.inheritance.service;

import de.kontextwork.poc.spring.many2many.inheritance.domain.ChildInheritanceBased;
import de.kontextwork.poc.spring.many2many.inheritance.domain.ChildParentAssociation;
import de.kontextwork.poc.spring.many2many.inheritance.domain.ParentInheritanceBased;
import de.kontextwork.poc.spring.many2many.inheritance.repository.ChildAssociationRepository;
import de.kontextwork.poc.spring.many2many.inheritance.repository.ChildInheritanceBasedRepository;
import de.kontextwork.poc.spring.many2many.inheritance.repository.ParentInheritanceBasedRepository;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ParentInheritanceService {
  private final ChildAssociationRepository childAssociationRepository;
  private final ChildInheritanceBasedRepository childInheritanceBasedRepository;
  private final ParentInheritanceBasedRepository parentInheritanceBasedRepository;

  public Optional<ParentInheritanceBased> getParent(Long parentId) {
    var element = parentInheritanceBasedRepository.findById(parentId);
    if (element.isEmpty()) {
      return element;
    }

    var children = childInheritanceBasedRepository
        .findAllByChildParentMachine(element.orElseThrow().getMachine());
    element.get().setChildren(children);
    return element;
  }

  public ParentInheritanceBased save(final ParentInheritanceBased parent) {
    parentInheritanceBasedRepository.save(parent);
    // remove all current associations
    childAssociationRepository.deleteById_ParentMachine(parent.getMachine());
    Set<ChildInheritanceBased> children = new HashSet<>();
    parent.getChildren().forEach(child -> {
      childInheritanceBasedRepository.save(child);
      childAssociationRepository
          .save(new ChildParentAssociation(parent.getMachine(), child.getMachine()));
      // add the child to the new set which we will set on the paren
      children.add(child);
    });

    parent.setChildren(children);
    return parent;
  }
}
