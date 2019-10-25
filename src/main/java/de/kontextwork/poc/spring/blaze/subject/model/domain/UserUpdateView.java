package de.kontextwork.poc.spring.blaze.subject.model.domain;

import com.blazebit.persistence.view.*;
import de.kontextwork.poc.spring.blaze.subject.model.jpa.User;

@UpdatableEntityView
@EntityView(User.class)
public interface UserUpdateView extends UserIdView
{
  String getFirstName();

  void setFirstName(String firstName);

  String getLastName();

  void setLastName(String firstName);
}
