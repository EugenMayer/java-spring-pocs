package de.kontextwork.poc.spring.blaze.fullapp.authorization.model.permission.view;

import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.IdMapping;
import de.kontextwork.poc.spring.blaze.fullapp.authorization.model.permission.jpa.BasePermission;

@EntityView(BasePermission.class)
public interface BasePermissionIdView
{
  @IdMapping
  Long getId();
}
