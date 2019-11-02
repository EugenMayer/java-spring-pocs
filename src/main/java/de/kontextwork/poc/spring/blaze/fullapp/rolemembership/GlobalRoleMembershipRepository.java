package de.kontextwork.poc.spring.blaze.fullapp.rolemembership;

import de.kontextwork.poc.spring.blaze.fullapp.rolemembership.model.jpa.GlobalRoleMembership;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GlobalRoleMembershipRepository extends JpaRepository<GlobalRoleMembership, Long>
{
}
