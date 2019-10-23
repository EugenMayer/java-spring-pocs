package de.kontextwork.poc.spring.blaze.subject.model.jpa.role;

import de.kontextwork.poc.spring.blaze.subject.model.jpa.privilege.*;
import java.util.Set;
import javax.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
@DiscriminatorValue(Role.REALM_ROLE_DISCRIMINATOR)
public class RealmRole extends Role<RealmPrivilege>
{
  public RealmRole(String name, Set<RealmPrivilege> privileges)
  {
    super(name, privileges);
  }
}
