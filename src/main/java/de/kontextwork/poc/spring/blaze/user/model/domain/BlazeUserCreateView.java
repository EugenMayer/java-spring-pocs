package de.kontextwork.poc.spring.blaze.user.model.domain;

import com.blazebit.persistence.view.*;
import de.kontextwork.poc.spring.blaze.user.model.jpa.BlazeUser;

@EntityView(BlazeUser.class)
@CreatableEntityView
public interface BlazeUserCreateView extends BlazeUserIdView
{
  @PostCreate
  default void init()
  {
    setLastName("");
    setFirstName("");
  }

  void setUid(Integer uid);

  String getLastName();

  void setLastName(String lastName);

  String getFirstName();

  void setFirstName(String firstName);

  String getUserName();

  void setUserName(String userName);
}
