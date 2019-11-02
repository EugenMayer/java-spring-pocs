package de.kontextwork.poc.spring.blaze.fullapp.realm.model.view;

import com.blazebit.persistence.view.AttributeFilter;
import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.filter.EqualFilter;
import de.kontextwork.poc.spring.blaze.fullapp.realm.model.jpa.Realm;
import de.kontextwork.poc.spring.blaze.fullapp.role.model.jpa.Role;
import de.kontextwork.poc.spring.blaze.fullapp.role.model.view.RoleIdView;

@EntityView(Realm.class)
public interface RealmView extends RealmIdView
{
  @AttributeFilter(EqualFilter.class)
  String getRealmName();
}
