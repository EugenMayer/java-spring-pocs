package de.kontextwork.poc.spring.blaze.fullapp.rolemembership.model.view;

import com.blazebit.persistence.view.*;
import de.kontextwork.poc.spring.blaze.fullapp.realm.model.view.RealmIdView;
import de.kontextwork.poc.spring.blaze.fullapp.role.model.view.RoleIdView;
import de.kontextwork.poc.spring.blaze.fullapp.rolemembership.model.jpa.GlobalRoleMembership;
import de.kontextwork.poc.spring.blaze.fullapp.rolemembership.model.jpa.RealmRoleMembership;
import de.kontextwork.poc.spring.blaze.fullapp.subject.model.view.SubjectIdView;
import org.apache.tomcat.jni.Global;

@EntityView(GlobalRoleMembership.class)
@CreatableEntityView
public interface GlobalRoleMembershipCreateView extends GlobalRoleMembershipIdView
{
  @IdMapping
  GlobalRoleMembershipEmbeddedIdView getId();

  void setRole(RoleIdView role);
  void setSubject(SubjectIdView role);
}
