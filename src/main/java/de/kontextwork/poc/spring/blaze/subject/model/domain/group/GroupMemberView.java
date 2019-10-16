package de.kontextwork.poc.spring.blaze.subject.model.domain.group;

import com.blazebit.persistence.view.EntityView;
import de.kontextwork.poc.spring.blaze.subject.model.jpa.User;

@EntityView(User.class)
public interface GroupMemberView
{
  String getFirstName();

  String getLastName();
}
