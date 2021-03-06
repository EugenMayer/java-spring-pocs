package de.kontextwork.poc.spring.blaze.fullapp.rolemembership.model.view;

import com.blazebit.persistence.view.EntityView;
import de.kontextwork.poc.spring.blaze.fullapp.rolemembership.model.jpa.RealmRoleMembershipId;

@EntityView(RealmRoleMembershipId.class)
public interface RealmRoleMembershipEmbeddedIdView
{
  Long getRoleId();

  Long getRealmId();

  Long getSubjectId();
}
