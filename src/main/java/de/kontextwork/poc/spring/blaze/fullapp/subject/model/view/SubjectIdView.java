package de.kontextwork.poc.spring.blaze.fullapp.subject.model.view;

import com.blazebit.persistence.view.*;
import com.blazebit.persistence.view.filter.EqualFilter;
import de.kontextwork.poc.spring.blaze.fullapp.subject.model.jpa.Subject;

@EntityView(Subject.class)
public interface SubjectIdView
{
  @IdMapping
  Long getId();
}
