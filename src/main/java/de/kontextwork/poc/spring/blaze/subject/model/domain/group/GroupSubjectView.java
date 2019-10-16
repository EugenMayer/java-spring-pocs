package de.kontextwork.poc.spring.blaze.subject.model.domain.group;

import com.blazebit.persistence.view.EntityView;
import de.kontextwork.poc.spring.blaze.subject.model.jpa.Group;
import java.util.List;

@EntityView(Group.class)
public interface GroupSubjectView extends GroupIdView
{
  String getName();

  List<GroupMemberView> getMembers();
}
