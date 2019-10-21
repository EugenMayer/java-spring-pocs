package de.kontextwork.poc.spring.blaze.subject.model.jpa.member;

import de.kontextwork.poc.spring.blaze.subject.model.jpa.Role;
import de.kontextwork.poc.spring.blaze.subject.model.jpa.Subject;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
@DiscriminatorValue(RoleMember.GLOBAL)
public class GlobalRoleMember extends RoleMember
{
  public GlobalRoleMember(final Role role, final Subject subject)
  {
    super(role, subject);
  }
}
