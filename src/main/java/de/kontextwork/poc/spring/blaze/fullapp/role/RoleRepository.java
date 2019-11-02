package de.kontextwork.poc.spring.blaze.fullapp.role;

import de.kontextwork.poc.spring.blaze.fullapp.role.model.jpa.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long>
{
}
