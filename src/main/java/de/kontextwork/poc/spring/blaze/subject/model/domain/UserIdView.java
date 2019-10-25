package de.kontextwork.poc.spring.blaze.subject.model.domain;

import com.blazebit.persistence.view.EntityView;
import de.kontextwork.poc.spring.blaze.subject.model.jpa.User;

@EntityView(User.class)
public interface UserIdView extends SubjectIdView
{
  Long getUid();

  void setUid(Long uid);
}
