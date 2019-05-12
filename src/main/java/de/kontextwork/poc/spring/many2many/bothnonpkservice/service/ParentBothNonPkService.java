package de.kontextwork.poc.spring.many2many.bothnonpkservice.service;

import de.kontextwork.poc.spring.many2many.bothnonpkservice.domain.ParentBothNonPkServiceBased;
import de.kontextwork.poc.spring.many2many.bothnonpkservice.repository.ChildBothNonPkServiceBasedRepository;
import de.kontextwork.poc.spring.many2many.bothnonpkservice.repository.ParentBothNonPkServiceBasedRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ParentBothNonPkService {
  private final ParentBothNonPkServiceBasedRepository parentBothNonPkServiceBasedRepository;
  private final ChildBothNonPkServiceBasedRepository childBothNonPkServiceBasedRepository;

  public Optional<ParentBothNonPkServiceBased> getParent(Long parentId) {
    var parent = parentBothNonPkServiceBasedRepository.findByParentId(parentId);
    if (parent.isEmpty()) {
      return parent;
    }

    var children = childBothNonPkServiceBasedRepository
        .findAllByChildParentMachine(parent.get().getMachine());
    parent.get().setChildren(children);
    return parent;
  }

  public ParentBothNonPkServiceBased save(ParentBothNonPkServiceBased parent) {
    parent = parentBothNonPkServiceBasedRepository.saveAndFlush(parent);
    parent = this.getParent(parent.getParentId()).orElseThrow();
    return parent;
  }
}
