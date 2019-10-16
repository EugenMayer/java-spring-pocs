package de.kontextwork.poc.spring.blaze.subject.model.domain.role;

import com.blazebit.persistence.view.*;
import com.blazebit.persistence.view.filter.EqualFilter;
import de.kontextwork.poc.spring.blaze.subject.model.jpa.Role;

@EntityView(Role.class)
public interface RoleView
{
  @IdMapping
  Long getId();

  @AttributeFilter(EqualFilter.class)
  String getName();
}
