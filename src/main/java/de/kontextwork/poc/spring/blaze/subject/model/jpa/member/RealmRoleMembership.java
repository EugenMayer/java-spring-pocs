package de.kontextwork.poc.spring.blaze.subject.model.jpa.member;

import de.kontextwork.poc.spring.blaze.subject.model.jpa.role.Role;
import de.kontextwork.poc.spring.blaze.subject.model.jpa.subject.Realm;
import de.kontextwork.poc.spring.blaze.subject.model.jpa.subject.Subject;
import javax.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
@DiscriminatorValue(RoleMembership.REALM_ROLE_DISCRIMINATOR)
public class RealmRoleMembership extends RoleMembership
{
  @OneToOne(fetch = FetchType.LAZY)
  private Realm realm;

  public RealmRoleMembership(final Realm realm, final Role role, final Subject subject)
  {
    super(role, subject);
    this.realm = realm;
  }
}
