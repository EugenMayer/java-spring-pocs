package de.kontextwork.poc.spring.blaze.fullapp.rolemembership;

import de.kontextwork.poc.spring.blaze.fullapp.rolemembership.model.jpa.GlobalRoleMembership;
import de.kontextwork.poc.spring.blaze.fullapp.role.model.jpa.Role;
import de.kontextwork.poc.spring.blaze.fullapp.subject.model.jpa.Subject;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GlobalRoleMembershipService
{
  private final GlobalRoleMembershipRepository globalRoleMembershipRepository;

  /**
   * Creates new {@link GlobalRoleMembership} from provided {@code role} and {@code subject}.
   */
  public void assign(final Role role, final Subject subject)
  {
    globalRoleMembershipRepository.save(new GlobalRoleMembership(role, subject));
  }
}
