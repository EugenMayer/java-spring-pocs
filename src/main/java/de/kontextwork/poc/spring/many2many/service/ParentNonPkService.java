package de.kontextwork.poc.spring.many2many.service;

import de.kontextwork.poc.spring.many2many.domain.nonpkservice.ParentNonPkServiceBased;
import de.kontextwork.poc.spring.many2many.repository.ChildNonPkServiceBasedRepository;
import de.kontextwork.poc.spring.many2many.repository.ParentNonPkServiceBasedRepository;
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
        .findAllByChildParentId(parent.get().getParentId());
    parent.get().setChildren(children);
    return parent;
  }

  public ParentNonPkServiceBased save(ParentNonPkServiceBased parent) {
    parent = parentNonPkServiceBasedRepository.saveAndFlush(parent);
    parent = this.getParent(parent.getParentId()).orElseThrow();
    return parent;
  }
}
