package de.kontextwork.poc.spring.blaze.subject.model.jpa.member;

import de.kontextwork.poc.spring.blaze.subject.model.jpa.subject.Subject;
import de.kontextwork.poc.spring.blaze.subject.model.jpa.role.Role;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
@DiscriminatorValue(RoleMembership.GLOBAL_ROLE_DISCRIMINATOR)
public class GlobalRoleMembership extends RoleMembership
{
  public GlobalRoleMembership(final Role role, final Subject subject)
  {
    super(role, subject);
  }
}
