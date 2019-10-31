package de.kontextwork.poc.spring.blaze.subject.model.domain;

import com.blazebit.persistence.view.*;
import de.kontextwork.poc.spring.blaze.subject.model.jpa.member.RealmRoleMembership;

@CreatableEntityView
@EntityView(RealmRoleMembership.class)
public interface RealmRoleMembershipCreateView
{
  @IdMapping("this")
  RealmRoleMembershipIdView getId();

  void setId(RealmRoleMembershipIdView id);
}