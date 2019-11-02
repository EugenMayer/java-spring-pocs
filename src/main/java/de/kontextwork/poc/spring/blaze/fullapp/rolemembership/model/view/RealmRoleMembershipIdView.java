package de.kontextwork.poc.spring.blaze.fullapp.rolemembership.model.view;

import com.blazebit.persistence.view.*;
import de.kontextwork.poc.spring.blaze.fullapp.rolemembership.model.jpa.RealmRoleMembership;

@EntityView(RealmRoleMembership.class)
public interface RealmRoleMembershipIdView
{
  @IdMapping
  RealmRoleMembershipEmbeddedIdView getId();
}
