package de.kontextwork.poc.spring.blaze.fullapp.rolemembership.model.view;

import lombok.*;

@Data
@Builder
public class RealmRoleMembershipCreateViewDTO implements RealmRoleMembershipCreateView
{
  private RealmRoleMembershipEmbeddedIdCreateView id;
}
