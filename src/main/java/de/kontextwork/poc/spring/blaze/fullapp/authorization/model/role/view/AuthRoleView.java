package de.kontextwork.poc.spring.blaze.fullapp.authorization.model.role.view;

import com.blazebit.persistence.view.*;
import com.blazebit.persistence.view.filter.EqualFilter;
import de.kontextwork.poc.spring.blaze.fullapp.authorization.model.Scope;
import de.kontextwork.poc.spring.blaze.fullapp.authorization.model.role.jpa.AuthRole;

@EntityView(AuthRole.class)
@EntityViewInheritance({SpaceAuthRoleView.class})
public interface AuthRoleView extends AuthRoleIdView
{
  @AttributeFilter(EqualFilter.class)
  String getName();

  @AttributeFilter(EqualFilter.class)
  String getMachineName();

  @AttributeFilter(EqualFilter.class)
  Scope getScope();

  @AttributeFilter(EqualFilter.class)
  boolean getIsVisible();
}
