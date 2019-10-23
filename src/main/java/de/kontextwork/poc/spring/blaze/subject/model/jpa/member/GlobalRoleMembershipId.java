package de.kontextwork.poc.spring.blaze.subject.model.jpa.member;

import java.io.Serializable;
import javax.persistence.Embeddable;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.Assert;

@Data
@Embeddable
@NoArgsConstructor
public class GlobalRoleMembershipId implements Serializable
{
  private Long roleId;
  private Long subjectId;

  public GlobalRoleMembershipId(final GlobalRoleMembership membership)
  {
    Assert.notNull(membership.getRole().getId(), "Role is unmanaged!");
    Assert.notNull(membership.getSubject().getId(), "Subject is unmanaged!");

    this.roleId = membership.getRole().getId();
    this.subjectId = membership.getSubject().getId();
  }
}
