package de.kontextwork.poc.spring.blaze.fullapp.privilege.model.view;

import com.blazebit.persistence.view.*;
import com.blazebit.persistence.view.filter.EqualFilter;
import de.kontextwork.poc.spring.blaze.fullapp.privilege.model.jpa.Privilege;

@EntityView(Privilege.class)
public interface PrivilegeView
{
  @IdMapping
  Long getId();

  @AttributeFilter(EqualFilter.class)
  String getName();
}
