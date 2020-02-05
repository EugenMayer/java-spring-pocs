package de.kontextwork.poc.spring.blaze.fullapp.authorization;

import de.kontextwork.poc.spring.blaze.fullapp.authorization.model.role.jpa.AuthRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthRoleRepository extends JpaRepository<AuthRole, Long>
{
}
