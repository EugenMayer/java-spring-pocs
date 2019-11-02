package de.kontextwork.poc.spring.blaze.fullapp.subject.group.model.view;

import com.blazebit.persistence.view.EntityView;
import de.kontextwork.poc.spring.blaze.fullapp.subject.group.model.jpa.Group;
import de.kontextwork.poc.spring.blaze.fullapp.subject.user.model.view.UserView;
import de.kontextwork.poc.spring.blaze.fullapp.subject.model.domain.SubjectView;
import java.util.Set;

@EntityView(Group.class)
public interface GroupView extends SubjectView
{
  String getName();

  Set<UserView> getMembers();
}
