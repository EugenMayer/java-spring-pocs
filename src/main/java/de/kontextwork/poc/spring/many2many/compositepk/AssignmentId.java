package de.kontextwork.poc.spring.many2many.compositepk;

import de.kontextwork.poc.spring.many2many.compositepk.user.User;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.*;

/**
 * Sample composite primary key to {@link User} entities.
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

  @Column(name = "user_id")
  private Long userId;

  @Column(name = "role_id")
  private Long roleId;
}
