package de.kontextwork.poc.spring.many2many.composite;

import de.kontextwork.poc.spring.many2many.composite.space.Space;
import de.kontextwork.poc.spring.many2many.composite.spacerole.SpaceRole;
import de.kontextwork.poc.spring.many2many.composite.user.User;
import java.util.Objects;
import javax.persistence.*;
import lombok.*;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "na_space_role_membership")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class SpaceRoleMembership
{
  @EmbeddedId
  @EqualsAndHashCode.Include
  private SpaceRoleMembershipId spaceRoleMembershipId = new SpaceRoleMembershipId();

  @ManyToOne(fetch = FetchType.LAZY)
  // that does reference the field-name in SpaceRoleMembershipId, not the actual column name
  @MapsId("spaceName")
  @JoinColumn(name = "space_name", columnDefinition = "INT(10)")
  private Space space;

  @ManyToOne(fetch = FetchType.LAZY)
  // that does reference the field-name in SpaceRoleMembershipId, not the actual column name
  @MapsId("userUsername")
  @JoinColumn(name = "user_username", columnDefinition = "INT(10)")
  private User user;

  @ManyToOne(fetch = FetchType.LAZY)
  // that does reference the field-name in SpaceRoleMembershipId, not the actual column name
  @MapsId("spaceRoleName")
  @JoinColumn(name = "space_role_name", columnDefinition = "INT(10)")
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

  /**
   * Ensures that the bi-directional association is managed when we create a new SpaceRoleMembership
   * This is important, otherwise the L1 cache will not be updated for our SpaceRole, loading the entity
   * using a repository would then not return any relations yet, we would need to either clear the L1 cache
   * before loading the spaceRole or fixing it like defined
   *
   * @param spaceRole
   */
  public void setSpaceRole(final SpaceRole spaceRole)
  {
    this.spaceRole = spaceRole;
    var members = spaceRole.getSpaceRoleMemberships();
    members.add(this);
    spaceRole.setSpaceRoleMemberships(members);
  }
}