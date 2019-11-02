package de.kontextwork.poc.spring.blaze.fullapp.rolemembership.model.jpa;

import de.kontextwork.poc.spring.blaze.fullapp.role.model.jpa.Role;
import de.kontextwork.poc.spring.blaze.fullapp.realm.model.jpa.Realm;
import de.kontextwork.poc.spring.blaze.fullapp.subject.model.jpa.Subject;
import javax.persistence.*;
import lombok.*;

@Data
@Entity
@NoArgsConstructor
@Table(name = "subject_realm_role_memberships")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class RealmRoleMembership
{
  @EmbeddedId
  @EqualsAndHashCode.Include
  private RealmRoleMembershipId id;

  @MapsId("roleId")
  @EqualsAndHashCode.Include
  @JoinColumn(name = "role_id", columnDefinition = "INT(10) UNSIGNED")
  @ManyToOne(fetch = FetchType.LAZY)
  private Role role;

  @MapsId("subjectId")
  @EqualsAndHashCode.Include
  @JoinColumn(name = "subject_id", columnDefinition = "INT(10) UNSIGNED")
  @ManyToOne(fetch = FetchType.LAZY)
  private Subject subject;

  @MapsId("realmId")
  @EqualsAndHashCode.Include
  @JoinColumn(name = "realm_id", columnDefinition = "INT(10) UNSIGNED")
  @ManyToOne(fetch = FetchType.LAZY)
  private Realm realm;

  public RealmRoleMembership(final Realm realm, final Role role, final Subject subject)
  {
    this.realm = realm;
    this.role = role;
    this.subject = subject;
    this.id = new RealmRoleMembershipId(this);
  }
}
