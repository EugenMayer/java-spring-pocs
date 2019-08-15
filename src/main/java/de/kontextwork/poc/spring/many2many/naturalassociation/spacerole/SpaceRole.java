package de.kontextwork.poc.spring.many2many.naturalassociation.spacerole;

import de.kontextwork.poc.spring.many2many.naturalassociation.LegacyMapping;
import java.util.List;
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

  @OneToMany(mappedBy = "spaceRole", cascade = CascadeType.ALL)
  private List<LegacyMapping> legacyMappings;
}
