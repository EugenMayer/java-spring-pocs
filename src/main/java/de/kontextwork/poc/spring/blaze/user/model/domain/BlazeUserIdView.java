package de.kontextwork.poc.spring.blaze.user.model.domain;

import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.IdMapping;
import de.kontextwork.poc.spring.blaze.user.model.jpa.BlazeUser;

@EntityView(BlazeUser.class)
public interface BlazeUserIdView
{
  @SuppressWarnings("unused")
  @IdMapping
  Integer getUid();
}
