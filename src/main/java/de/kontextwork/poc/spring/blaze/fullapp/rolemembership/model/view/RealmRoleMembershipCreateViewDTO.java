package de.kontextwork.poc.spring.blaze.fullapp.rolemembership.model.view;

import de.kontextwork.poc.spring.blaze.fullapp.realm.model.jpa.Realm;
import de.kontextwork.poc.spring.blaze.fullapp.realm.model.view.RealmIdView;
import de.kontextwork.poc.spring.blaze.fullapp.role.model.jpa.Role;
import de.kontextwork.poc.spring.blaze.fullapp.role.model.view.RoleIdView;
import de.kontextwork.poc.spring.blaze.fullapp.subject.model.jpa.Subject;
import de.kontextwork.poc.spring.blaze.fullapp.subject.model.view.SubjectIdView;
import lombok.*;

@Data
@Builder
public class RealmRoleMembershipCreateViewDTO implements RealmRoleMembershipCreateView
{
  private RealmRoleMembershipEmbeddedIdCreateView id;
  private RoleIdView role;
  private SubjectIdView subject;
  private RealmIdView realm;
}
