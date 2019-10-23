package de.kontextwork.poc.spring.blaze.subject.model.jpa.role;

import de.kontextwork.poc.spring.blaze.subject.model.jpa.privilege.GlobalPrivilege;
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
