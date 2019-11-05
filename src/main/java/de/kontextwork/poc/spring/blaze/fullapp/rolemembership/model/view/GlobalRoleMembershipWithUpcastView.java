package de.kontextwork.poc.spring.blaze.fullapp.rolemembership.model.view;

import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.IdMapping;
import de.kontextwork.poc.spring.blaze.fullapp.role.model.view.RoleIdView;
import de.kontextwork.poc.spring.blaze.fullapp.rolemembership.model.jpa.GlobalRoleMembership;
import de.kontextwork.poc.spring.blaze.fullapp.subject.model.view.SubjectExcerptView;

@EntityView(GlobalRoleMembership.class)
public interface GlobalRoleMembershipWithUpcastView extends GlobalRoleMembershipIdView
{
  @Override
  RoleIdView getRole();

  @Override
  SubjectExcerptView getSubject();
}
