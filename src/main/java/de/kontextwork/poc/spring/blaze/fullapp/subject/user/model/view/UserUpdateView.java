package de.kontextwork.poc.spring.blaze.fullapp.subject.user.model.view;

import com.blazebit.persistence.view.*;
import de.kontextwork.poc.spring.blaze.fullapp.subject.user.model.jpa.User;

@UpdatableEntityView
@EntityView(User.class)
public interface UserUpdateView extends UserIdView
{
  String getFirstName();

  void setFirstName(String firstName);

  String getLastName();

  void setLastName(String firstName);
}
