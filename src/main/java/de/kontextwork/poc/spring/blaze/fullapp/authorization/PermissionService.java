package de.kontextwork.poc.spring.blaze.fullapp.authorization;

import com.blazebit.persistence.CriteriaBuilderFactory;
import com.blazebit.persistence.view.EntityViewManager;
import de.kontextwork.poc.spring.blaze.fullapp.authorization.model.permission.view.PermissionCreateView;
import de.kontextwork.poc.spring.blaze.fullapp.authorization.model.role.jpa.AuthRole;
import javax.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.modelmapper.internal.util.Assert;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PermissionService
{
  private final EntityManager entityManager;
  private final EntityViewManager entityViewManager;
  private final CriteriaBuilderFactory criteriaBuilderFactory;

  public boolean hasPermission(@NonNull String machineName)
  {
    Assert.state(machineName.startsWith("__"), "Invalid machineName");

    return !criteriaBuilderFactory.create(entityManager, AuthRole.class)
      .select("1")
      .where("TREAT(basePermissions AS CompositePermission).permissions.machineName").eq(machineName)
      .setMaxResults(1)
      .getResultList()
      .isEmpty();
  }

  @Transactional
  public <E> void saveFull(final E entityCreateView)
  {
    entityViewManager.saveFull(entityManager, entityCreateView);
  }
}
