package de.kontextwork.poc.spring.blaze.subject.model.jpa.member;

import java.io.Serializable;
import javax.persistence.*;
import lombok.*;
import org.springframework.util.Assert;

@Data
@Embeddable
@NoArgsConstructor
@SuppressWarnings("WeakerAccess")
public class RealmRoleMembershipId implements Serializable
{
  private Long roleId;
  private Long subjectId;
  private Long realmId;

  public RealmRoleMembershipId(final RealmRoleMembership membership)
  {
    Assert.notNull(membership.getRole().getId(), "Role is unmanaged!");
    Assert.notNull(membership.getSubject().getId(), "Subject is unmanaged!");
    Assert.notNull(membership.getRealm().getId(), "Realm is unmanaged!");

    this.roleId = membership.getRole().getId();
    this.subjectId = membership.getSubject().getId();
    this.realmId = membership.getRealm().getId();
  }
}
