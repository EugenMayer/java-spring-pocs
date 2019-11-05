package de.kontextwork.poc.spring.blaze.fullapp.subject.model.view;

import com.blazebit.persistence.view.*;
import de.kontextwork.poc.spring.blaze.fullapp.subject.model.view.SubjectFilter.GlobalRoleFilter;
import de.kontextwork.poc.spring.blaze.fullapp.subject.model.jpa.Subject;

@EntityView(Subject.class)
@EntityViewInheritance
@ViewFilter(name = "USER_IN_GLOBAL_ROLE_USER", value = GlobalRoleFilter.User.class)
@ViewFilter(name = "USER_IN_GLOBAL_ROLE_MODERATOR", value = GlobalRoleFilter.Moderator.class)
@ViewFilter(name = "USER_IN_GLOBAL_ROLE_ADMINISTRATOR", value = GlobalRoleFilter.Administrator.class)
public interface SubjectExcerptView extends SubjectIdView
{
}
