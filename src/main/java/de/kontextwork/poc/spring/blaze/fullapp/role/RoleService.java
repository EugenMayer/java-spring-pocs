package de.kontextwork.poc.spring.blaze.fullapp.role;

import de.kontextwork.poc.spring.blaze.fullapp.role.model.jpa.Role;
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
