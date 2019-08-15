package de.kontextwork.poc.spring.many2many.ahhocassociation;

import de.kontextwork.poc.spring.many2many.ahhocassociation.person.Person;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.*;

/**
 * Sample composite primary key to {@link Person} entities.
 *
 * @author Sebastian Ullrich
 */
@Builder
@Embeddable
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class AssignmentId implements Serializable
{
  @Column(name = "company_id")
  private Long companyId;

  @Column(name = "person_id")
  private Long personId;

  @Column(name = "role_id")
  private Long roleId;
}
