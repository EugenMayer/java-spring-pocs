package de.kontextwork.poc.spring.blaze.subject.model.domain;

import com.blazebit.persistence.view.*;
import de.kontextwork.poc.spring.blaze.subject.model.jpa.member.RealmRoleMembership;

@EntityView(RealmRoleMembership.class)
public interface RealmRoleMembershipIdView
{
  @IdMapping
  RealmRoleMembershipEmbeddedIdView getId();
}
