package de.kontextwork.poc.spring.blaze.subject.model.domain;

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
