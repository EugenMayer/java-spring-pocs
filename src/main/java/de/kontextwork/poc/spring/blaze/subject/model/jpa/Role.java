package de.kontextwork.poc.spring.blaze.subject.model.jpa;

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

  private String name;

  @OneToOne(mappedBy = "role", fetch = FetchType.LAZY)
  private RoleMember roleMember;

  public Role(final String name)
  {
    this.name = name;
  }
}
