package de.kontextwork.poc.spring.blaze.subject.model.domain.user;

import com.blazebit.persistence.view.*;
import de.kontextwork.poc.spring.blaze.subject.model.jpa.User;

@EntityView(User.class)
public interface UserIdView
{
  @IdMapping
  Long getId();

  Long getUid();
}
