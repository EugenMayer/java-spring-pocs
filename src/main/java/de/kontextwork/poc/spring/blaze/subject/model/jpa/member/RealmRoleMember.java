package de.kontextwork.poc.spring.blaze.subject.model.jpa.member;

import de.kontextwork.poc.spring.blaze.subject.model.jpa.*;
import javax.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
@DiscriminatorValue(RoleMember.REALM)
public class RealmRoleMember extends RoleMember
{
  @OneToOne(fetch = FetchType.LAZY)
  private Realm realm;

  public RealmRoleMember(final Realm realm, final Role role, final Subject subject)
  {
    super(role, subject);
    this.realm = realm;
  }
}
