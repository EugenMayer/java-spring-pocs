package de.kontextwork.poc.spring.blaze.fullapp.privilege;

import de.kontextwork.poc.spring.blaze.fullapp.privilege.model.jpa.Privilege;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrivilegeRepository extends JpaRepository<Privilege, Long>
{
}
