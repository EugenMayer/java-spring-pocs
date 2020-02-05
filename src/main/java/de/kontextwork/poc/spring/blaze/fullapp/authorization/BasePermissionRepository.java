package de.kontextwork.poc.spring.blaze.fullapp.authorization;

import de.kontextwork.poc.spring.blaze.fullapp.authorization.model.permission.jpa.BasePermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BasePermissionRepository extends JpaRepository<BasePermission, Long>
{
}
