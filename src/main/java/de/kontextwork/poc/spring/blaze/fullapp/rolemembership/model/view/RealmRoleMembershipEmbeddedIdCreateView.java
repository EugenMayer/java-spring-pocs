package de.kontextwork.poc.spring.blaze.fullapp.rolemembership.model.view;

import com.blazebit.persistence.view.*;
import de.kontextwork.poc.spring.blaze.fullapp.rolemembership.model.jpa.RealmRoleMembershipId;

@EntityView(RealmRoleMembershipId.class)
@CreatableEntityView
@UpdatableEntityView
public interface RealmRoleMembershipEmbeddedIdCreateView extends RealmRoleMembershipEmbeddedIdView
{
  void setSubjectId(Long subjectId);

  Long getRealmId();

  void setRealmId(Long realmId);
}
