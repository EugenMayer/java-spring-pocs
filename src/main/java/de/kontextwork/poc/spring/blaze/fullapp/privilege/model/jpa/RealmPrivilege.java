package de.kontextwork.poc.spring.blaze.fullapp.privilege.model.jpa;

import de.kontextwork.poc.spring.blaze.fullapp.role.model.jpa.RealmRole;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
@DiscriminatorValue(Privilege.REALM_PRIVILEGE_DISCRIMINATOR)
public class RealmPrivilege extends Privilege<RealmRole>
{
  public RealmPrivilege(String name)
  {
    super(name);
  }
}
