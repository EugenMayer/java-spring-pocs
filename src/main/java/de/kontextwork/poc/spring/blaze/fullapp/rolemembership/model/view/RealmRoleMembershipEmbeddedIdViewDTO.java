package de.kontextwork.poc.spring.blaze.fullapp.rolemembership.model.view;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RealmRoleMembershipEmbeddedIdViewDTO implements RealmRoleMembershipEmbeddedIdView
{
  private Long roleId;
  private Long subjectId;
  private Long realmId;
}
