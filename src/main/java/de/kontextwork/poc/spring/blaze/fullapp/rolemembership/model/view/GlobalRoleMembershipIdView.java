package de.kontextwork.poc.spring.blaze.fullapp.rolemembership.model.view;

import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.IdMapping;
import de.kontextwork.poc.spring.blaze.fullapp.rolemembership.model.jpa.GlobalRoleMembership;
import de.kontextwork.poc.spring.blaze.fullapp.rolemembership.model.jpa.RealmRoleMembership;
import org.apache.tomcat.jni.Global;

@EntityView(GlobalRoleMembership.class)
public interface GlobalRoleMembershipIdView
{
  @IdMapping
  GlobalRoleMembershipEmbeddedIdView getId();
}
