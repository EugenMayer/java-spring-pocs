package de.kontextwork.poc.spring.blaze.fullapp.subject.group.model.view;

import com.blazebit.persistence.view.*;
import de.kontextwork.poc.spring.blaze.fullapp.subject.group.model.jpa.Group;
import de.kontextwork.poc.spring.blaze.fullapp.subject.user.model.view.UserIdView;
import java.util.Set;

@EntityView(Group.class)
@UpdatableEntityView(strategy = FlushStrategy.ENTITY, mode = FlushMode.FULL)
public interface GroupMemberUpdateView extends GroupIdView
{
  Set<UserIdView> getMembers();
  void setMembers(Set<UserIdView> members);
}
