package de.kontextwork.poc.spring.blaze.subject.model.domain;

import com.blazebit.persistence.view.*;
import com.blazebit.persistence.view.filter.EqualFilter;
import de.kontextwork.poc.spring.blaze.subject.model.jpa.subject.Subject;

@EntityView(Subject.class)
public interface SubjectIdView
{
  @IdMapping
  @AttributeFilters({@AttributeFilter(value = EqualFilter.class)})
  Long getId();
}
