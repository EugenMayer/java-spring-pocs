package de.kontextwork.poc.spring.many2many.compositepk;

import de.kontextwork.poc.spring.many2many.compositepk.company.Company;
import de.kontextwork.poc.spring.many2many.compositepk.role.Role;
import de.kontextwork.poc.spring.many2many.compositepk.user.User;
import javax.persistence.*;
import lombok.*;

/**
 * This entity represents a 'legacy' relation table that has no foreign key relation with
 * {@link User}, {@link Company} or {@link Role}.
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
