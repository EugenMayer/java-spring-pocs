package de.kontextwork.poc.spring.many2many.naturalassociation;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.Data;

@Data
@Embeddable
class SpaceRoleMembershipPk implements Serializable
{
  @Column(name = "space_name", length = 10)
  private String spaceName;

  @Column(name = "user_account_username", length = 10)
  private String userAccountUsername;

  @Column(name = "space_role_name", length = 10)
  private String spaceRoleName;
}
