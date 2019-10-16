package de.kontextwork.poc.spring.blaze.subject.model.domain.group;

import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.IdMapping;
import de.kontextwork.poc.spring.blaze.subject.model.jpa.Group;

@EntityView(Group.class)
public interface GroupIdView
{
  @IdMapping
  Long getId();
}
