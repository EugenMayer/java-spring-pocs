package de.kontextwork.poc.spring.blaze.subject.model.domain;

import com.blazebit.persistence.view.EntityView;
import de.kontextwork.poc.spring.blaze.subject.model.jpa.member.RealmRoleMembership;
import de.kontextwork.poc.spring.blaze.subject.model.jpa.member.RealmRoleMembershipId;

@EntityView(RealmRoleMembershipId.class)
public interface RealmRoleMembershipEmbeddedIdView
{
  Long getRoleId();

  void setRoleId(Long roleId);

  Long getSubjectId();

  void setSubjectId(Long subjectId);

  Long getRealmId();

  void setRealmId(Long realmId);
}
