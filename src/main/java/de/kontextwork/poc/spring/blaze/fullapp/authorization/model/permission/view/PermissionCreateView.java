package de.kontextwork.poc.spring.blaze.fullapp.authorization.model.permission.view;

import com.blazebit.persistence.view.*;
import de.kontextwork.poc.spring.blaze.fullapp.authorization.model.Scope;
import de.kontextwork.poc.spring.blaze.fullapp.authorization.model.permission.jpa.Permission;

@CreatableEntityView
@EntityView(Permission.class)
public interface PermissionCreateView extends PermissionView
{
  void setName(String name);

  void setScope(Scope scope);

  void setMachineName(String machineName);

  void setIsVisible(boolean isVisible);
}
