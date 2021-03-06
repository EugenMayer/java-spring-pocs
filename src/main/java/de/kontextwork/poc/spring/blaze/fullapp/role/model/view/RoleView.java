package de.kontextwork.poc.spring.blaze.fullapp.role.model.view;

import com.blazebit.persistence.view.*;
import com.blazebit.persistence.view.filter.EqualFilter;
import de.kontextwork.poc.spring.blaze.fullapp.role.model.jpa.Role;

@EntityView(Role.class)
public interface RoleView extends RoleIdView
{
  @AttributeFilter(EqualFilter.class)
  String getName();
}
