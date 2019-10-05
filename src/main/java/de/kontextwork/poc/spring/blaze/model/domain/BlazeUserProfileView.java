package de.kontextwork.poc.spring.blaze.model.domain;

import com.blazebit.persistence.view.EntityView;
import de.kontextwork.poc.spring.blaze.model.jpa.BlazeUser;

@SuppressWarnings("unused")
@EntityView(BlazeUser.class)
public interface BlazeUserProfileView extends BlazeUserIdView
{
  String getUserName();

  String getFirstName();

  String getLastName();
}
