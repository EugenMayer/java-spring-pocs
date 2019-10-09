package de.kontextwork.poc.spring.blaze.user.model.domain;

import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.UpdatableEntityView;
import de.kontextwork.poc.spring.blaze.user.model.jpa.BlazeUser;

@SuppressWarnings("unused")
@EntityView(BlazeUser.class)
@UpdatableEntityView
public interface BlazeUserProfileUpdateView extends BlazeUserProfileView
{
  void setUserName(String userName);

  void setFirstName(String firstName);

  void setLastName(String lastName);
}
