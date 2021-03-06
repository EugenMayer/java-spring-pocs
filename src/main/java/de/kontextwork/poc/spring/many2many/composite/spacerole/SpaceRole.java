package de.kontextwork.poc.spring.many2many.composite.spacerole;

import de.kontextwork.poc.spring.many2many.composite.SpaceRoleMembership;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import lombok.*;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "na_space_role")
public class SpaceRole
{
  @Id
  @Column(length = 10)
  private String name;

  @OneToMany(mappedBy = "spaceRole")
  private Set<SpaceRoleMembership> spaceRoleMemberships = new HashSet<>();

  public SpaceRole(final String name)
  {
    this.name = name;
  }
}
