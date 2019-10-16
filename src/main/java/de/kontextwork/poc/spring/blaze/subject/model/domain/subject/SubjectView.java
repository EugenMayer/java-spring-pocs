package de.kontextwork.poc.spring.blaze.subject.model.domain.subject;

import com.blazebit.persistence.view.*;
import de.kontextwork.poc.spring.blaze.subject.model.jpa.Subject;

@EntityView(Subject.class)
@EntityViewInheritance
public interface SubjectView
{
  @IdMapping
  Long getId();
}
