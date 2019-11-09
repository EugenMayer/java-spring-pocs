package de.kontextwork.poc.spring.blaze.fullapp.testUtils.scenario;

import com.blazebit.persistence.view.EntityViewManager;
import com.blazebit.persistence.view.EntityViewSetting;
import com.google.common.collect.Sets;
import de.kontextwork.poc.spring.blaze.fullapp.subject.group.GroupService;
import de.kontextwork.poc.spring.blaze.fullapp.subject.group.model.view.*;
import de.kontextwork.poc.spring.blaze.fullapp.subject.user.UserSerivce;
import de.kontextwork.poc.spring.blaze.fullapp.subject.user.model.view.UserCreateView;
import de.kontextwork.poc.spring.blaze.fullapp.subject.user.model.view.UserIdView;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserScenarioCreator
{
  private final UserSerivce userSerivce;
  private final EntityViewManager entityViewManager;

  public UserIdView createUser(String firstName, String lastName) {
    UserCreateView userCreateView = entityViewManager.create(UserCreateView.class);
    userCreateView.setFirstName(firstName);
    userCreateView.setLastName(lastName);

    return userSerivce.create(userCreateView);
  }
}
