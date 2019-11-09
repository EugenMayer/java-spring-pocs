package de.kontextwork.poc.spring.blaze.fullapp.testUtils.scenario;

import com.blazebit.persistence.view.EntityViewManager;
import com.blazebit.persistence.view.EntityViewSetting;
import com.google.common.collect.Sets;
import de.kontextwork.poc.spring.blaze.fullapp.subject.group.GroupService;
import de.kontextwork.poc.spring.blaze.fullapp.subject.group.model.view.*;
import de.kontextwork.poc.spring.blaze.fullapp.subject.user.model.view.UserCreateView;
import de.kontextwork.poc.spring.blaze.fullapp.subject.user.model.view.UserIdView;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GroupScenarioCreator
{
  private final GroupService groupService;
  private final EntityViewManager entityViewManager;

  public GroupIdView createGroup(String name, String machine) {
    GroupCreateView groupCreateView = entityViewManager.create(GroupCreateView.class);
    groupCreateView.setName(name);
    groupCreateView.setGroupMachine(machine);

    return groupService.create(groupCreateView);
  }


  public void limitUsersTo(GroupIdView groupIdView, UserIdView... newMembers) {
    GroupMemberUpdateView groupMemberUpdateView = entityViewManager.getReference(
      GroupMemberUpdateView.class,
      groupIdView.getId()
    );
    groupMemberUpdateView.setMembers(Sets.newHashSet(newMembers));
    groupService.limitMembersTo(groupIdView.getId(), groupMemberUpdateView);
  }

  public  GroupEntireView getFullGroup(GroupIdView groupIdView) {
    return groupService.getOne(
      EntityViewSetting.create(GroupEntireView.class), groupIdView.getId()
    ).orElseThrow();
  }
}
