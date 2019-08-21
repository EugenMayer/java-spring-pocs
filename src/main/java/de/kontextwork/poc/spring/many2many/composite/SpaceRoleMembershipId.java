package de.kontextwork.poc.spring.many2many.composite;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Embeddable
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
class SpaceRoleMembershipId implements Serializable
{
  @EqualsAndHashCode.Include
  @Column(name = "space_name", length = 10)
  private String spaceName;

  @EqualsAndHashCode.Include
  @Column(name = "user_username", length = 10)
  private String userUsername;

  @EqualsAndHashCode.Include
  @Column(name = "space_role_name", length = 10)
  private String spaceRoleName;
}
