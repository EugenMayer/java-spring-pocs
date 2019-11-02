package de.kontextwork.poc.spring.blaze.fullapp.rolemembership.model.view;

import com.blazebit.persistence.view.EntityView;
import de.kontextwork.poc.spring.blaze.fullapp.role.model.jpa.GlobalRole;
import de.kontextwork.poc.spring.blaze.fullapp.rolemembership.model.jpa.GlobalRoleMembershipId;
import de.kontextwork.poc.spring.blaze.fullapp.rolemembership.model.jpa.RealmRoleMembershipId;

@EntityView(GlobalRoleMembershipId.class)
public interface GlobalRoleMembershipEmbeddedIdView
{
  Long getRoleId();

  Long getSubjectId();
}
