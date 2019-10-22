package de.kontextwork.poc.spring.blaze.subject.model.domain;

import com.blazebit.persistence.view.*;
import com.blazebit.persistence.view.filter.EqualFilter;
import de.kontextwork.poc.spring.blaze.subject.model.jpa.role.Role;

@EntityView(Role.class)
public interface RoleView
{
  @IdMapping
  Long getId();

  @AttributeFilter(EqualFilter.class)
  String getName();
}
