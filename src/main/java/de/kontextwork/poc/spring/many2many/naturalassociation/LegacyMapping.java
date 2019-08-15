package de.kontextwork.poc.spring.many2many.naturalassociation;

import de.kontextwork.poc.spring.many2many.naturalassociation.space.Space;
import de.kontextwork.poc.spring.many2many.naturalassociation.spacerole.SpaceRole;
import de.kontextwork.poc.spring.many2many.naturalassociation.useraccount.UserAccount;
import javax.persistence.*;
import lombok.*;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "na_legacy_mapping")
public class LegacyMapping
{
  @EmbeddedId
  @Builder.Default
  private LegacyMappingId legacyMappingId = new LegacyMappingId();

  @ManyToOne(fetch = FetchType.LAZY)
  @MapsId("spaceName")
  private Space space;

  @ManyToOne(fetch = FetchType.LAZY)
  @MapsId("userAccountUsername")
  private UserAccount userAccount;

  @ManyToOne(fetch = FetchType.LAZY)
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
      userAccount.getUsername(),
      space.getName(),
      spaceRole.getName());
  }
}