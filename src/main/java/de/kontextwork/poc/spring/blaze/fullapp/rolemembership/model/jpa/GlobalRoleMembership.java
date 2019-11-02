package de.kontextwork.poc.spring.blaze.fullapp.rolemembership.model.jpa;

import de.kontextwork.poc.spring.blaze.fullapp.subject.model.jpa.Subject;
import de.kontextwork.poc.spring.blaze.fullapp.role.model.jpa.Role;
import javax.persistence.*;
import lombok.*;

@Data
@Entity
@NoArgsConstructor
@Table(name = "subject_global_role_memberships")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class GlobalRoleMembership
{
  @EmbeddedId
  @EqualsAndHashCode.Include
  private GlobalRoleMembershipId id;

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

  public GlobalRoleMembership(final Role role, final Subject subject)
  {
    this.role = role;
    this.subject = subject;
    this.id = new GlobalRoleMembershipId(this);
  }
}
