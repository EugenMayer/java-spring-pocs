package de.kontextwork.poc.spring.blaze.fullapp.subject.user.model.view;

import com.blazebit.persistence.view.*;
import de.kontextwork.poc.spring.blaze.fullapp.subject.user.model.jpa.User;

@CreatableEntityView
@EntityView(User.class)
public interface UserCreateView extends UserIdView
{
  String getFirstName();

  void setFirstName(String firstName);

  String getLastName();

  void setLastName(String firstName);
}
