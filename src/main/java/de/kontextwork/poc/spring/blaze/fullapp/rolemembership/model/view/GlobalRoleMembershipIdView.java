package de.kontextwork.poc.spring.blaze.fullapp.rolemembership.model.view;

import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.IdMapping;
import de.kontextwork.poc.spring.blaze.fullapp.role.model.view.RoleIdView;
import de.kontextwork.poc.spring.blaze.fullapp.rolemembership.model.jpa.GlobalRoleMembership;
import de.kontextwork.poc.spring.blaze.fullapp.rolemembership.model.jpa.RealmRoleMembership;
import de.kontextwork.poc.spring.blaze.fullapp.subject.model.view.SubjectIdView;
import org.apache.tomcat.jni.Global;

@EntityView(GlobalRoleMembership.class)
public interface GlobalRoleMembershipIdView
{
  @IdMapping
  GlobalRoleMembershipEmbeddedIdView getId();

  RoleIdView getRole();
  SubjectIdView getSubject();
}
