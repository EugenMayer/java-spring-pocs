package de.kontextwork.poc.spring.many2many.naturalassociation;

import de.kontextwork.poc.spring.many2many.naturalassociation.space.Space;
import de.kontextwork.poc.spring.many2many.naturalassociation.spacerole.SpaceRole;
import de.kontextwork.poc.spring.many2many.naturalassociation.user.User;
import javax.persistence.*;
import lombok.*;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "na_space_role_membership")
public class SpaceRoleMembership
{
  @EmbeddedId
  @Builder.Default
  private SpaceRoleMembershipPk spaceRoleMembershipPk = new SpaceRoleMembershipPk();

  @ManyToOne(fetch = FetchType.LAZY)
  // that does reference the field-name in SpaceRoleMembershipPk, not the actual column name
  @MapsId("spaceName")
  private Space space;

  @ManyToOne(fetch = FetchType.LAZY)
  // that does reference the field-name in SpaceRoleMembershipPk, not the actual column name
  @MapsId("userUsername")
  private User user;

  @ManyToOne(fetch = FetchType.LAZY)
  // that does reference the field-name in SpaceRoleMembershipPk, not the actual column name
  @MapsId("spaceRoleName")
  private SpaceRole spaceRole;

  @Column(name = "comment", length = 50)
  private String comment;

  /**
   * Constructs permission string `{username}/{space}/{role}`
   */
  public String toString()
  {
    return String.format("%s/%s/%s",
      user.getUsername(),
      space.getName(),
      spaceRole.getName());
  }
}