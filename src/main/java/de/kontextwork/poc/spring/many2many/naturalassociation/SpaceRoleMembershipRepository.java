package de.kontextwork.poc.spring.many2many.naturalassociation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpaceRoleMembershipRepository extends JpaRepository<SpaceRoleMembership, SpaceRoleMembershipId>
{
}
