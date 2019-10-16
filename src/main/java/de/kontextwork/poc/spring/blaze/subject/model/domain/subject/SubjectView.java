package de.kontextwork.poc.spring.blaze.subject.model.domain.subject;

import com.blazebit.persistence.view.*;
import de.kontextwork.poc.spring.blaze.subject.model.domain.subject.SubjectViewFilter.*;
import de.kontextwork.poc.spring.blaze.subject.model.jpa.Subject;

@EntityView(Subject.class)
@EntityViewInheritance
@ViewFilters({
  @ViewFilter(name = "userRoleFiler", value = UserRoleFilterProvider.class),
  @ViewFilter(name = "moderatorRoleFilter", value = ModeratorRoleFilter.class),
  @ViewFilter(name = "administratorRoleFilter", value = AdministratorRoleFilter.class)
})
public interface SubjectView
{
  @IdMapping
  Long getId();
}
