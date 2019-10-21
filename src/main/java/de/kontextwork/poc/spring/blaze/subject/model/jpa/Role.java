package de.kontextwork.poc.spring.blaze.subject.model.jpa;

import de.kontextwork.poc.spring.blaze.subject.model.jpa.member.RoleMember;
import javax.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;

@Data
@Entity
@Accessors(chain = true)
@Table(name = "subject_role")
public class Role
{
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  /**
   * The roles name in uppercase.
   */
  private String name;

  /**
   * Owner of this {@link Role}.
   */
  @OneToOne(mappedBy = "role", fetch = FetchType.LAZY)
  private RoleMember roleMember;

  public Role(final String name)
  {
    this.name = name;
  }
}
