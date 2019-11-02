package de.kontextwork.poc.spring.blaze.subject;

import de.kontextwork.poc.spring.blaze.subject.model.jpa.privilege.Privilege;
import javax.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PrivilegeService
{
  private final PrivilegeRepository privilegeRepository;
  private final EntityManager entityManager;

  /**
   * Creates new {@link Privilege}.
   */
  public <P extends Privilege> P create(final P privilege)
  {
    return privilegeRepository.save(privilege);
  }
}
