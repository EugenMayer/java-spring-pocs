package de.kontextwork.poc.spring.blaze.fullapp.subject.group.model.view;

import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.Mapping;
import de.kontextwork.poc.spring.blaze.fullapp.subject.group.model.jpa.Group;
import de.kontextwork.poc.spring.blaze.fullapp.subject.user.model.view.UserExcerptView;
import de.kontextwork.poc.spring.blaze.fullapp.subject.model.view.SubjectExcerptView;
import java.util.Set;

@EntityView(Group.class)
public interface GroupExcerptView extends SubjectExcerptView, GroupIdView
{
  String getName();

  Set<UserExcerptView> getMembers();

  @Mapping("SIZE(members)")
  Long getMemberCount();
}
