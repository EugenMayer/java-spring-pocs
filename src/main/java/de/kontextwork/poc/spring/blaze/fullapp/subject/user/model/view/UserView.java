package de.kontextwork.poc.spring.blaze.fullapp.subject.user.model.view;

import com.blazebit.persistence.view.*;
import de.kontextwork.poc.spring.blaze.fullapp.subject.model.view.SubjectView;
import de.kontextwork.poc.spring.blaze.fullapp.subject.user.model.jpa.User;

@EntityView(User.class)
public interface UserView extends SubjectView
{
  String getFirstName();

  String getLastName();
}
