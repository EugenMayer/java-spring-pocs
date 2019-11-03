package de.kontextwork.poc.spring.blaze.fullapp.rolemembership.model.view;

import com.blazebit.persistence.view.*;
import de.kontextwork.poc.spring.blaze.fullapp.realm.model.view.RealmIdView;
import de.kontextwork.poc.spring.blaze.fullapp.role.model.view.RoleIdView;
import de.kontextwork.poc.spring.blaze.fullapp.rolemembership.model.jpa.RealmRoleMembership;
import de.kontextwork.poc.spring.blaze.fullapp.subject.model.view.SubjectIdView;

@EntityView(RealmRoleMembership.class)
public interface RealmRoleMembershipIdView
{
  @IdMapping
  RealmRoleMembershipEmbeddedIdView getId();

  RoleIdView getRole();
  SubjectIdView getSubject();
  RealmIdView getRealm();
}
