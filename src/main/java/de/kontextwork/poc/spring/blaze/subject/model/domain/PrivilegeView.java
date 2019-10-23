package de.kontextwork.poc.spring.blaze.subject.model.domain;

import com.blazebit.persistence.view.*;
import com.blazebit.persistence.view.filter.EqualFilter;
import de.kontextwork.poc.spring.blaze.subject.model.jpa.privilege.Privilege;

@EntityView(Privilege.class)
public interface PrivilegeView
{
  @IdMapping
  Long getId();

  @AttributeFilter(EqualFilter.class)
  String getName();
}
