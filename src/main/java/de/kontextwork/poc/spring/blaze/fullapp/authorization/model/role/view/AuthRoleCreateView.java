package de.kontextwork.poc.spring.blaze.fullapp.authorization.model.role.view;

import com.blazebit.persistence.view.*;
import de.kontextwork.poc.spring.blaze.fullapp.authorization.model.Scope;
import de.kontextwork.poc.spring.blaze.fullapp.authorization.model.role.jpa.AuthRole;

@CreatableEntityView
@EntityView(AuthRole.class)
public interface AuthRoleCreateView extends AuthRoleView
{
  void setName(String name);

  void setMachineName(String machineName);

  void setScope(Scope scope);

  void setIsVisible(boolean isVisible);
}
