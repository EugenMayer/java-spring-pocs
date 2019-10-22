package de.kontextwork.poc.spring.blaze.subject.model.jpa.privilege;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
@DiscriminatorValue(Privilege.REALM_PRIVILEGE_DISCRIMINATOR)
public class RealmPrivilege extends Privilege
{
  public RealmPrivilege(String name)
  {
    super(name);
  }
}
