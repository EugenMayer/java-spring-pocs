package de.kontextwork.poc.spring.blaze.fullapp.authorization.model.permission.view;

import com.blazebit.persistence.view.*;
import de.kontextwork.poc.spring.blaze.fullapp.authorization.model.Scope;
import de.kontextwork.poc.spring.blaze.fullapp.authorization.model.permission.jpa.CompositePermission;
import de.kontextwork.poc.spring.blaze.fullapp.authorization.model.permission.jpa.Permission;
import java.util.Set;

@CreatableEntityView
@EntityView(CompositePermission.class)
public interface CompositePermissionCreateView extends CompositePermissionView
{
  void setName(String name);

  void setScope(Scope scope);

  void setMachineName(String machineName);

  void setIsVisible(boolean isVisible);

  @UpdatableMapping(cascade = CascadeType.PERSIST)
  void setPermissions(Set<PermissionView> permissions);
}
