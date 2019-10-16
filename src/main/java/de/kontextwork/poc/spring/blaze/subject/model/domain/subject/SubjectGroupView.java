package de.kontextwork.poc.spring.blaze.subject.model.domain.subject;

import com.blazebit.persistence.view.EntityView;
import de.kontextwork.poc.spring.blaze.subject.model.jpa.Group;

@EntityView(Group.class)
public interface SubjectGroupView extends SubjectView
{
  String getName();
}
