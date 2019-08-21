package de.kontextwork.poc.spring.many2many.naturalassociation.spacerole;

import de.kontextwork.poc.spring.many2many.naturalassociation.SpaceRoleMembership;
import java.util.*;
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

  public SpaceRole(final String name)
  {
    this.name = name;
  }

  @OneToMany(mappedBy = "spaceRole")
  private Set<SpaceRoleMembership> spaceRoleMemberships = new HashSet<>();
}
