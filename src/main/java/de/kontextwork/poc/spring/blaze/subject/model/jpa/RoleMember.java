package de.kontextwork.poc.spring.blaze.subject.model.jpa;

import javax.persistence.*;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Entity
@Accessors(chain = true)
@Table(name = "subject_role_member")
public class RoleMember
{
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @OneToOne(fetch = FetchType.LAZY)
  private Role role;

  @OneToOne(fetch = FetchType.LAZY)
  private Subject subject;

  public RoleMember(final Role role, final Subject subject)
  {
    this.role = role;
    this.subject = subject;
  }
}
