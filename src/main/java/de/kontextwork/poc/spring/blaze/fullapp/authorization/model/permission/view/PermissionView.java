package de.kontextwork.poc.spring.blaze.fullapp.authorization.model.permission.view;

import com.blazebit.persistence.view.EntityView;
import de.kontextwork.poc.spring.blaze.fullapp.authorization.model.Scope;
import de.kontextwork.poc.spring.blaze.fullapp.authorization.model.permission.jpa.Permission;

@EntityView(Permission.class)
public interface PermissionView extends BasePermissionIdView
{
  String getName();

  Scope getScope();

  String getMachineName();

  boolean getIsVisible();
}
