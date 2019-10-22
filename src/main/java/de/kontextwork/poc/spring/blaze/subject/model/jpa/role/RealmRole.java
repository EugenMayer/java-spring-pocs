package de.kontextwork.poc.spring.blaze.subject.model.jpa.role;

import de.kontextwork.poc.spring.blaze.subject.model.jpa.privilege.Privilege;
import java.util.Set;
import javax.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
@DiscriminatorValue(Role.REALM_ROLE_DISCRIMINATOR)
public class RealmRole extends Role
{
  public RealmRole(String name, Set<Privilege> privileges)
  {
    super(name, privileges);
  }
}
