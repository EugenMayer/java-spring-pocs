package de.kontextwork.poc.spring.blaze.fullapp.authorization.model.permission.view;

import com.blazebit.persistence.view.EntityView;
import de.kontextwork.poc.spring.blaze.fullapp.authorization.model.Scope;
import de.kontextwork.poc.spring.blaze.fullapp.authorization.model.permission.jpa.CompositePermission;
import java.util.Set;

@EntityView(CompositePermission.class)
public interface CompositePermissionView extends BasePermissionIdView
{
  String getName();

  Scope getScope();

  String getMachineName();

  boolean getIsVisible();

  Set<PermissionView> getPermissions();
}
