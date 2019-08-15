package de.kontextwork.poc.spring.many2many.ahhocassociation.person;

import javax.persistence.*;
import lombok.*;

/**
 * Sample entity that is identified by a composite primary key.
 *
 * @author Sebastian Ullrich
 */
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Person
{
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  private String firstName;
  private String lastName;
}
