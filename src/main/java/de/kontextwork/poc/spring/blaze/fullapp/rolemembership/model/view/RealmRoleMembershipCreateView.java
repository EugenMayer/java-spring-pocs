package de.kontextwork.poc.spring.blaze.fullapp.rolemembership.model.view;

import com.blazebit.persistence.view.*;
import de.kontextwork.poc.spring.blaze.fullapp.realm.model.jpa.Realm;
import de.kontextwork.poc.spring.blaze.fullapp.realm.model.view.RealmIdView;
import de.kontextwork.poc.spring.blaze.fullapp.role.model.jpa.Role;
import de.kontextwork.poc.spring.blaze.fullapp.role.model.view.RoleIdView;
import de.kontextwork.poc.spring.blaze.fullapp.rolemembership.model.jpa.RealmRoleMembership;
import de.kontextwork.poc.spring.blaze.fullapp.subject.model.jpa.Subject;
import de.kontextwork.poc.spring.blaze.fullapp.subject.model.view.SubjectIdView;

@EntityView(RealmRoleMembership.class)
@CreatableEntityView
public interface RealmRoleMembershipCreateView extends RealmRoleMembershipIdView
{
  void setRole(RoleIdView role);
  void setSubject(SubjectIdView role);
  void setRealm(RealmIdView role);
}
