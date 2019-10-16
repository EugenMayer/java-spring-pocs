package de.kontextwork.poc.spring.blaze.subject.model.domain.user;

import com.blazebit.persistence.view.EntityView;
import de.kontextwork.poc.spring.blaze.subject.model.jpa.*;

@EntityView(User.class)
public interface UserSubjectView extends UserIdView
{
  String getFirstName();

  String getLastName();
}
