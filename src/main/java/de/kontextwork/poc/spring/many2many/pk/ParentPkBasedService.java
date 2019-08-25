package de.kontextwork.poc.spring.many2many.pk;

import de.kontextwork.poc.spring.many2many.pk.domain.ParentPkBasedDTO;
import de.kontextwork.poc.spring.many2many.pk.repository.ParentPkBasedRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ParentPkBasedService
{
  final ParentPkBasedRepository parentPkBasedRepository;

  public List<ParentPkBasedDTO> getAllParentsProjected() {
    return parentPkBasedRepository.findAllProjectedBy(ParentPkBasedDTO.class);
  }
}
