package de.kontextwork.poc.spring.blaze.subject;

import de.kontextwork.poc.spring.blaze.subject.model.jpa.member.GlobalRoleMembership;
import de.kontextwork.poc.spring.blaze.subject.model.jpa.role.Role;
import de.kontextwork.poc.spring.blaze.subject.model.jpa.subject.Subject;
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
