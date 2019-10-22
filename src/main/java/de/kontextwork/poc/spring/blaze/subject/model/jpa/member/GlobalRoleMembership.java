package de.kontextwork.poc.spring.blaze.subject.model.jpa.member;

import de.kontextwork.poc.spring.blaze.subject.model.jpa.subject.Subject;
import de.kontextwork.poc.spring.blaze.subject.model.jpa.role.Role;
import javax.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "subject_global_role_memberships")
public class GlobalRoleMembership
{
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @OneToOne(fetch = FetchType.LAZY)
  private Role role;

  @OneToOne(fetch = FetchType.LAZY)
  private Subject subject;

  public GlobalRoleMembership(final Role role, final Subject subject)
  {
    this.role = role;
    this.subject = subject;
  }
}
