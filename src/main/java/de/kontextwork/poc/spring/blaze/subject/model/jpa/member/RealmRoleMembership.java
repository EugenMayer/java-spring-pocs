package de.kontextwork.poc.spring.blaze.subject.model.jpa.member;

import de.kontextwork.poc.spring.blaze.subject.model.jpa.role.Role;
import de.kontextwork.poc.spring.blaze.subject.model.jpa.subject.Realm;
import de.kontextwork.poc.spring.blaze.subject.model.jpa.subject.Subject;
import javax.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "subject_realm_role_memberships")
public class RealmRoleMembership
{
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @OneToOne(fetch = FetchType.LAZY)
  private Role role;

  @OneToOne(fetch = FetchType.LAZY)
  private Subject subject;

  @OneToOne(fetch = FetchType.LAZY)
  private Realm realm;

  public RealmRoleMembership(final Realm realm, final Role role, final Subject subject)
  {
    this.realm = realm;
    this.role = role;
    this.subject = subject;
  }
}
