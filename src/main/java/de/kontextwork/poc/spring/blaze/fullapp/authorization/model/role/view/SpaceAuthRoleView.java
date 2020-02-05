package de.kontextwork.poc.spring.blaze.fullapp.authorization.model.role.view;

import com.blazebit.persistence.view.*;
import de.kontextwork.poc.spring.blaze.fullapp.authorization.model.role.jpa.AuthRole;

@EntityView(AuthRole.class)
@EntityViewInheritanceMapping("scope = 'SPACE'")
public interface SpaceAuthRoleView extends AuthRoleView
{
  //@Mapping("compositePermissions.permissions")
  //List<PermissionView> getPermissions();
}
