package de.kontextwork.poc.spring.blaze.fullapp.subject.user.model.view;

import com.blazebit.persistence.view.*;
import de.kontextwork.poc.spring.blaze.fullapp.subject.model.view.SubjectExcerptView;
import de.kontextwork.poc.spring.blaze.fullapp.subject.user.model.jpa.User;

@EntityView(User.class)
public interface UserExcerptView extends SubjectExcerptView, UserIdView
{
  String getFirstName();

  String getLastName();
}
