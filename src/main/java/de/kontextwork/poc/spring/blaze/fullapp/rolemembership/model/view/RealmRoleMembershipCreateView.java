package de.kontextwork.poc.spring.blaze.fullapp.rolemembership.model.view;

import com.blazebit.persistence.view.*;
import de.kontextwork.poc.spring.blaze.fullapp.realm.model.jpa.Realm;
import de.kontextwork.poc.spring.blaze.fullapp.role.model.jpa.Role;
import de.kontextwork.poc.spring.blaze.fullapp.rolemembership.model.jpa.RealmRoleMembership;
import de.kontextwork.poc.spring.blaze.fullapp.subject.model.jpa.Subject;

@EntityView(RealmRoleMembership.class)
@CreatableEntityView
public interface RealmRoleMembershipCreateView extends RealmRoleMembershipIdView
{
  @IdMapping
  RealmRoleMembershipEmbeddedIdCreateView getId();

  void setId(RealmRoleMembershipEmbeddedIdCreateView id);

  void setRole(Role role);
  void setSubject(Subject role);
  void setRealm(Realm role);

  Role getRole();
  Subject getSubject();
  Realm getRealm();
}
