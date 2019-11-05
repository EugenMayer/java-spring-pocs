package de.kontextwork.poc.spring.blaze.fullapp.rolemembership.model.view;

import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.IdMapping;
import de.kontextwork.poc.spring.blaze.fullapp.realm.model.view.RealmIdView;
import de.kontextwork.poc.spring.blaze.fullapp.role.model.view.RoleIdView;
import de.kontextwork.poc.spring.blaze.fullapp.rolemembership.model.jpa.GlobalRoleMembership;
import de.kontextwork.poc.spring.blaze.fullapp.rolemembership.model.jpa.RealmRoleMembership;
import de.kontextwork.poc.spring.blaze.fullapp.subject.model.view.SubjectExcerptView;

@EntityView(RealmRoleMembership.class)
public interface RealmRoleMembershipWithUpcastView extends RealmRoleMembershipIdView
{
  @Override
  RoleIdView getRole();

  @Override
  SubjectExcerptView getSubject();
}
