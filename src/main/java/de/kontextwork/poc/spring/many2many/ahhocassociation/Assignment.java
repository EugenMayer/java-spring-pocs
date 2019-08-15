package de.kontextwork.poc.spring.many2many.ahhocassociation;

import de.kontextwork.poc.spring.many2many.ahhocassociation.company.Company;
import de.kontextwork.poc.spring.many2many.ahhocassociation.role.Role;
import de.kontextwork.poc.spring.many2many.ahhocassociation.person.Person;
import javax.persistence.*;
import lombok.*;

/**
 * This entity represents a 'legacy' relation table that has no foreign key relation with
 * {@link Person}, {@link Company} or {@link Role}.
 */
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Assignment
{
  @EmbeddedId
  private AssignmentId assignmentId;
}
