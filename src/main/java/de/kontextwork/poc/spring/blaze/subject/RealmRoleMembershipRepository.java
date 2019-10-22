package de.kontextwork.poc.spring.blaze.subject;

import de.kontextwork.poc.spring.blaze.subject.model.jpa.member.RealmRoleMembership;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RealmRoleMembershipRepository extends JpaRepository<RealmRoleMembership, Long>
{
}
