package de.kontextwork.poc.spring.many2many.pk.web;

import de.kontextwork.poc.spring.many2many.pk.ParentPkBasedService;
import de.kontextwork.poc.spring.many2many.pk.domain.ParentPkBasedDTO;
import de.kontextwork.poc.spring.many2many.pk.repository.ParentPkBasedRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pk/parent")
public class ParentPkBasedController
{
  @Autowired
  ParentPkBasedService parentPkBasedService;

  @GetMapping
  public ResponseEntity<List<ParentPkBasedDTO>> getAllParents()
  {
      return ResponseEntity.ok(parentPkBasedService.getAllParentsProjected());
  }
}
