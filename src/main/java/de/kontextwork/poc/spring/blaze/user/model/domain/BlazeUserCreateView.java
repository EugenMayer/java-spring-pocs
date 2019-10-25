package de.kontextwork.poc.spring.blaze.user.model.domain;

import com.blazebit.persistence.view.*;
import de.kontextwork.poc.spring.blaze.user.model.jpa.BlazeUser;

@CreatableEntityView
@SuppressWarnings("unused")
@EntityView(BlazeUser.class)
public interface BlazeUserCreateView extends BlazeUserIdView
{
  @PostCreate
  default void init()
  {
    setLastName("");
    setFirstName("");
  }

  String getLastName();

  void setLastName(String lastName);

  String getFirstName();

  void setFirstName(String firstName);

  String getUserName();

  void setUserName(String userName);
}
