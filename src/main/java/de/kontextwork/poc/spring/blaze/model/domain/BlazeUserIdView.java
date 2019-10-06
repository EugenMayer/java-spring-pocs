package de.kontextwork.poc.spring.blaze.model.domain;

import com.blazebit.persistence.view.*;
import de.kontextwork.poc.spring.blaze.model.jpa.BlazeUser;

@EntityView(BlazeUser.class)
@CreatableEntityView
public interface BlazeUserIdView
{
  @SuppressWarnings("unused")
  @IdMapping
  Integer getUid();

  void setUid(Integer uid);
}
