package de.kontextwork.poc.spring.blaze.subject;

import de.kontextwork.poc.spring.blaze.subject.model.jpa.role.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleService
{
  private final RoleRepository roleRepository;


  /**
   * Creates new {@link Role}.
   */
  public <R extends Role> R create(final R role)
  {
    return roleRepository.save(role);
  }
}
