package de.kontextwork.poc.spring.blaze.fullapp.role.model.jpa;

import de.kontextwork.poc.spring.blaze.fullapp.privilege.model.jpa.GlobalPrivilege;
import java.util.Set;
import javax.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
@DiscriminatorValue(Role.GLOBAL_ROLE_DISCRIMINATOR)
public class GlobalRole extends Role<GlobalPrivilege>
{
  public GlobalRole(String name, Set<GlobalPrivilege> privileges)
  {
    super(name, privileges);
  }
}
