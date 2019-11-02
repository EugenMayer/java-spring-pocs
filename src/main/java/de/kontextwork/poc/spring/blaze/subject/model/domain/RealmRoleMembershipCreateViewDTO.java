package de.kontextwork.poc.spring.blaze.subject.model.domain;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RealmRoleMembershipCreateViewDTO implements RealmRoleMembershipCreateView
{
  private RealmRoleMembershipEmbeddedIdView id;
}
