package de.kontextwork.poc.spring.blaze.subject.model.domain;

import com.blazebit.persistence.view.*;
import de.kontextwork.poc.spring.blaze.subject.model.jpa.member.RealmRoleMembership;
import de.kontextwork.poc.spring.blaze.subject.model.jpa.member.RealmRoleMembershipId;

@EntityView(RealmRoleMembership.class)
@CreatableEntityView
public interface RealmRoleMembershipCreateView extends RealmRoleMembershipIdView
{
  void setId(RealmRoleMembershipEmbeddedIdView id);
}
