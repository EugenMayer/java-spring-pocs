package de.kontextwork.poc.spring.many2many.ahhocassociation.role;

import de.kontextwork.poc.spring.many2many.ahhocassociation.person.Person;
import java.util.List;
import javax.persistence.*;
import lombok.*;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Role
{
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  private String name;

  @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  @JoinTable(name = "assignment",
    joinColumns = @JoinColumn(name = "role_id"),
    inverseJoinColumns = @JoinColumn(name = "person_id")
  )
  private List<Person> members;
}
