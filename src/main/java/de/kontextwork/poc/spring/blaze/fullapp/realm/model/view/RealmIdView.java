package de.kontextwork.poc.spring.blaze.fullapp.realm.model.view;

import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.IdMapping;
import de.kontextwork.poc.spring.blaze.fullapp.realm.model.jpa.Realm;
import de.kontextwork.poc.spring.blaze.fullapp.role.model.jpa.Role;

@EntityView(Realm.class)
public interface RealmIdView
{
  @IdMapping
  Long getId();
}
