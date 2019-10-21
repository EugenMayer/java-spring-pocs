package de.kontextwork.poc.spring.blaze.subject.model.domain;

import com.blazebit.persistence.view.*;
import de.kontextwork.poc.spring.blaze.subject.model.domain.filter.UserInRoleFilter;
import de.kontextwork.poc.spring.blaze.subject.model.jpa.Subject;

@EntityView(Subject.class)
@EntityViewInheritance
@ViewFilters({
  @ViewFilter(name = UserInRoleFilter.User.NAME, value = UserInRoleFilter.User.class),
  @ViewFilter(name = UserInRoleFilter.Moderator.NAME, value = UserInRoleFilter.Moderator.class),
  @ViewFilter(name = UserInRoleFilter.Administrator.NAME, value = UserInRoleFilter.Administrator.class),
})
public interface SubjectView
{
  @IdMapping
  Long getId();
}
