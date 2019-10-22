package de.kontextwork.poc.spring.blaze.subject.model.domain;

import com.blazebit.persistence.view.EntityView;
import de.kontextwork.poc.spring.blaze.subject.model.jpa.subject.Group;
import java.util.Set;

@EntityView(Group.class)
public interface SubjectGroupView extends SubjectView
{
  String getName();

  Set<SubjectUserView> getMembers();
}
