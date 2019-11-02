package de.kontextwork.poc.spring.blaze.fullapp.rolemembership.model.view;

import de.kontextwork.poc.spring.blaze.fullapp.realm.model.view.RealmIdView;
import de.kontextwork.poc.spring.blaze.fullapp.role.model.view.RoleIdView;
import de.kontextwork.poc.spring.blaze.fullapp.subject.model.view.SubjectIdView;
import lombok.Builder;
import lombok.Data;
import org.apache.tomcat.jni.Global;

@Data
@Builder
public class GlobalRoleMembershipCreateViewDTO implements GlobalRoleMembershipCreateView
{
  private GlobalRoleMembershipEmbeddedIdView id;
  private RoleIdView role;
  private SubjectIdView subject;
}
