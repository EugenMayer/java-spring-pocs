package de.kontextwork.poc.spring.blaze.model.domain;

import com.blazebit.persistence.view.CreatableEntityView;
import com.blazebit.persistence.view.EntityView;
import de.kontextwork.poc.spring.blaze.model.jpa.BlazeUser;

@EntityView(BlazeUser.class)
@CreatableEntityView
public interface BlazeUserIdView
{
  @SuppressWarnings("unused")
  Integer getUid();

  void setUid(Integer uid);
}
