package de.kontextwork.poc.spring.many2many.naturalassociation.spacerole;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpaceRoleRepository extends JpaRepository<SpaceRole, String>
{
}
