package de.kontextwork.poc.spring.many2many.composite;

import java.io.Serializable;
import javax.persistence.Embeddable;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Embeddable
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
class SpaceRoleMembershipId implements Serializable
{
  @EqualsAndHashCode.Include
  private String spaceName;

  @EqualsAndHashCode.Include
  private String userUsername;

  @EqualsAndHashCode.Include
  private String spaceRoleName;
}
