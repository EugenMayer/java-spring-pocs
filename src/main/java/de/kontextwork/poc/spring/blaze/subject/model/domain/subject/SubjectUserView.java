package de.kontextwork.poc.spring.blaze.subject.model.domain.subject;

import com.blazebit.persistence.view.EntityView;
import de.kontextwork.poc.spring.blaze.subject.model.jpa.User;

@EntityView(User.class)
public interface SubjectUserView extends SubjectView
{
  String getFirstName();

  String getLastName();
}
