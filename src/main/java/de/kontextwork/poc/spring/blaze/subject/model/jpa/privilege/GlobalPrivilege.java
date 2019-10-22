package de.kontextwork.poc.spring.blaze.subject.model.jpa.privilege;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
@DiscriminatorValue(Privilege.GLOBAL_PRIVILEGE_DISCRIMINATOR)
public class GlobalPrivilege extends Privilege
{
  public GlobalPrivilege(String name)
  {
    super(name);
  }
}
