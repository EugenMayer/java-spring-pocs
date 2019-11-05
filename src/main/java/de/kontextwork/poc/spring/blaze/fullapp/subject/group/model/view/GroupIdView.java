package de.kontextwork.poc.spring.blaze.fullapp.subject.group.model.view;

import com.blazebit.persistence.view.EntityView;
import de.kontextwork.poc.spring.blaze.fullapp.subject.group.model.jpa.Group;
import de.kontextwork.poc.spring.blaze.fullapp.subject.model.view.SubjectIdView;
import de.kontextwork.poc.spring.blaze.fullapp.subject.user.model.jpa.User;

@EntityView(Group.class)
public interface GroupIdView extends SubjectIdView
{
}
