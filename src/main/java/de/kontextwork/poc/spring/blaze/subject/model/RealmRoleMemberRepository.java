package de.kontextwork.poc.spring.blaze.subject.model;

import de.kontextwork.poc.spring.blaze.subject.model.jpa.member.RealmRoleMembership;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RealmRoleMemberRepository extends JpaRepository<RealmRoleMembership, Long>
{
}
