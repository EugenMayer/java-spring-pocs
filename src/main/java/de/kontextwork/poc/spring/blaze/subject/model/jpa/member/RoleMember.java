package de.kontextwork.poc.spring.blaze.subject.model.jpa.member;

import de.kontextwork.poc.spring.blaze.subject.model.jpa.Role;
import de.kontextwork.poc.spring.blaze.subject.model.jpa.Subject;
import javax.persistence.*;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Entity
@Accessors(chain = true)
@Table(name = "subject_role_member")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.STRING)
public abstract class RoleMember
{
  public static final String GLOBAL = "GLOBAL";
  public static final String REALM = "REALM";

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @OneToOne(fetch = FetchType.LAZY)
  private Role role;

  @OneToOne(fetch = FetchType.LAZY)
  private Subject subject;

  RoleMember(final Role role, final Subject subject)
  {
    this.role = role;
    this.subject = subject;
  }
}
