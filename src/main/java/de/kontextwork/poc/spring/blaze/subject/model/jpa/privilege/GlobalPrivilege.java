package de.kontextwork.poc.spring.blaze.subject.model.jpa.privilege;

import de.kontextwork.poc.spring.blaze.subject.model.jpa.role.GlobalRole;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
@DiscriminatorValue(Privilege.GLOBAL_PRIVILEGE_DISCRIMINATOR)
public class GlobalPrivilege extends Privilege<GlobalRole>
{
  public GlobalPrivilege(String name)
  {
    super(name);
  }
}
