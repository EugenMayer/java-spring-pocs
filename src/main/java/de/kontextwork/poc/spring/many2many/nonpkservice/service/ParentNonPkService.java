package de.kontextwork.poc.spring.many2many.nonpkservice.service;

import de.kontextwork.poc.spring.many2many.nonpkservice.domain.ParentNonPkServiceBased;
import de.kontextwork.poc.spring.many2many.nonpkservice.repository.ChildNonPkServiceBasedRepository;
import de.kontextwork.poc.spring.many2many.nonpkservice.repository.ParentNonPkServiceBasedRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ParentNonPkService {
  private final ParentNonPkServiceBasedRepository parentNonPkServiceBasedRepository;
  private final ChildNonPkServiceBasedRepository childNonPkServiceBasedRepository;

  public Optional<ParentNonPkServiceBased> getParent(Long parentId) {
    var parent = parentNonPkServiceBasedRepository.findByParentId(parentId);
    if (parent.isEmpty()) {
      return parent;
    }

    var children = childNonPkServiceBasedRepository
        .findAllByChildParentId(parent.orElseThrow().getParentId());
    parent.get().setChildren(children);
    return parent;
  }

  public ParentNonPkServiceBased save(ParentNonPkServiceBased parent) {
    parent = parentNonPkServiceBasedRepository.saveAndFlush(parent);
    parent = this.getParent(parent.getParentId()).orElseThrow();
    return parent;
  }
}
