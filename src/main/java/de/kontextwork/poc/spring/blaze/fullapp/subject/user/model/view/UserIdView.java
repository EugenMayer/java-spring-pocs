package de.kontextwork.poc.spring.blaze.fullapp.subject.user.model.view;

import com.blazebit.persistence.view.EntityView;
import de.kontextwork.poc.spring.blaze.fullapp.subject.model.domain.SubjectIdView;
import de.kontextwork.poc.spring.blaze.fullapp.subject.user.model.jpa.User;

@EntityView(User.class)
public interface UserIdView extends SubjectIdView
{
  Long getUid();

  void setUid(Long uid);
}
