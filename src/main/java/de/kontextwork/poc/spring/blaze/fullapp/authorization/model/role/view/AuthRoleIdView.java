package de.kontextwork.poc.spring.blaze.fullapp.authorization.model.role.view;

import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.IdMapping;
import de.kontextwork.poc.spring.blaze.fullapp.authorization.model.role.jpa.AuthRole;

@EntityView(AuthRole.class)
public interface AuthRoleIdView
{
  @IdMapping
  Long getId();
}
