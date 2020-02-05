package de.kontextwork.poc.spring.blaze.fullapp.authorization;

import com.blazebit.persistence.CriteriaBuilder;
import com.blazebit.persistence.CriteriaBuilderFactory;
import com.blazebit.persistence.view.EntityViewManager;
import com.blazebit.persistence.view.EntityViewSetting;
import com.google.common.collect.Sets;
import de.kontextwork.poc.spring.blaze.core.PageableEntityViewRepository;
import de.kontextwork.poc.spring.blaze.core.RegularEntityViewRepository;
import de.kontextwork.poc.spring.blaze.fullapp.authorization.model.Scope;
import de.kontextwork.poc.spring.blaze.fullapp.authorization.model.permission.jpa.*;
import de.kontextwork.poc.spring.blaze.fullapp.authorization.model.permission.view.*;
import de.kontextwork.poc.spring.blaze.fullapp.authorization.model.role.jpa.*;
import de.kontextwork.poc.spring.blaze.fullapp.authorization.model.role.view.AuthRoleCreateView;
import de.kontextwork.poc.spring.blaze.fullapp.authorization.model.role.view.AuthRoleView;
import de.kontextwork.poc.spring.configuration.BlazePersistenceConfiguration;
import de.kontextwork.poc.spring.configuration.JpaBlazeConfiguration;
import java.util.List;
import java.util.Set;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.AutoConfigureDataJdbc;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

@Import({
  JpaBlazeConfiguration.class,
  BlazePersistenceConfiguration.class,
  PageableEntityViewRepository.class,
  RegularEntityViewRepository.class,
  ModelMapper.class,
  PermissionService.class
})
@AutoConfigureDataJdbc
@DataJpaTest(properties = {"blazepersistance.enabled=true"})
class BasePermissionServiceTest
{
  @Autowired
  private BasePermissionRepository basePermissionRepository;

  @Autowired
  private AuthRoleRepository authRoleRepository;

  @Autowired
  private PermissionService permissionService;

  @Autowired
  CriteriaBuilderFactory criteriaBuilderFactory;

  @Autowired
  EntityViewManager entityViewManager;

  @Autowired
  EntityManager entityManager;

  @Test
  @DisplayName("Should cascade child permissions of composite permission")
  public void shouldCascadeChildPermissionsOfCompositePermission()
  {
    final long initialCount = basePermissionRepository.count();

    persist(new CompositePermission("Some Composite", Scope.SYSTEM, false, Set.of(
      new Permission("Permission Foo", Scope.SYSTEM, false),
      new Permission("Permission Bar", Scope.SYSTEM, false)
    )));

    assertThat(basePermissionRepository.count()).isEqualTo(initialCount + 3);
  }

  @Test
  @DisplayName("Should reject public permissions of scope SYSTEM")
  public void shouldRejectPublicPermissionsOfScopeSpace()
  {
    assertThatCode(() -> persist(new Permission("Read Space", Scope.SYSTEM, true)))
      .hasRootCauseExactlyInstanceOf(IllegalStateException.class)
      .hasMessageContaining("SYSTEM permissions must not be PUBLIC");
  }

  @Test
  @DisplayName("Should reject System Role with Space permission")
  public void shouldRejectSystemRoleWithSpacePermission()
  {
    CompositePermission compositePermission = persist(new CompositePermission("Random Space Composite Permission",
      Scope.SPACE, true, Set.of(
      new Permission("Some Space Permission Foo", Scope.SPACE, true),
      new Permission("Some Space Permission Bar", Scope.SPACE, true)
    )));

    assertThatCode(() -> persist(new AuthRole("Some System Role", Scope.SYSTEM, false, Set.of(compositePermission))))
      .hasRootCauseExactlyInstanceOf(IllegalStateException.class)
      .hasMessageContaining("COMPOSITE PERMISSIONS scope must match ROLE scope");
  }

  @Test
  @DisplayName("Should persist CompositePermissions")
  public void shouldPersisCompositePermissions()
  {
    permissionService
      .saveFull(buildCompositePermission("Administer System", "__administer_system", Scope.SYSTEM, false,
        Sets.newHashSet(
          buildPermission("Create System User", "__create_system_user", Scope.SYSTEM, false),
          buildPermission("Delete System User", "__delete_system_user", Scope.SYSTEM, false),
          buildPermission("Create System Topic", "__create_system_user", Scope.SYSTEM, false),
          buildPermission("Delete System Topic", "__delete_system_user", Scope.SYSTEM, false),
          buildPermission("Shutdown System", "__shutdown_system", Scope.SYSTEM, false)
        )));

    permissionService
      .saveFull(buildCompositePermission("Administer Space", "__administer_space", Scope.SPACE, true,
        Sets.newHashSet(
          buildPermission("Create Space Topic", "__create_space_topic", Scope.SPACE, true),
          buildPermission("Delete Space Topic", "__delete_space_topic", Scope.SPACE, true),
          buildPermission("NonPublic Space Permission", "__nonpublic_space_permission", Scope.SPACE, false)
        )));

    permissionService.saveFull(buildAuthRole("System Role Red", "__system_role_red", Scope.SPACE, true));
    permissionService.saveFull(buildAuthRole("System Role Blue", "__system_role_blue", Scope.SYSTEM, true));

    var setting = EntityViewSetting.create(AuthRoleView.class);
    setting.addAttributeFilter("scope", Scope.SPACE);
    CriteriaBuilder<AuthRole> criteriaBuilder = criteriaBuilderFactory.create(entityManager, AuthRole.class);
    List<AuthRoleView> resultList = entityViewManager.applySetting(setting, criteriaBuilder).getResultList();

    assertThat(resultList).hasSize(1);
    //assertThat(resultList.get(0).getPermissions()).hasSize(8);

    final PermissionCreateView permissionCreateView = entityViewManager.create(PermissionCreateView.class);
    permissionCreateView.setName("Create View Test");
    permissionCreateView.setMachineName("__create_view_test");
    permissionCreateView.setIsVisible(false);
    permissionCreateView.setScope(Scope.SYSTEM);

    permissionService.saveFull(permissionCreateView);
    //entityViewManager.saveFull(entityManager, permissionCreateView);

    // Basically checks if Role exists with Permission assigned
    assertThat(permissionService.hasPermission("__create_view_test")).isTrue();
    assertThat(permissionService.hasPermission("__create_system_user")).isTrue();
    assertThat(permissionService.hasPermission("__delete_system_user")).isTrue();
    assertThat(permissionService.hasPermission("__something_else")).isFalse();
  }

  private CompositePermissionCreateView buildCompositePermission(
    String name,
    String machineName,
    Scope scope,
    boolean isVisible,
    Set<PermissionView> permissions
  )
  {
    var compositePermissionCreateView = entityViewManager.create(CompositePermissionCreateView.class);
    compositePermissionCreateView.setName(name);
    compositePermissionCreateView.setMachineName(machineName);
    compositePermissionCreateView.setScope(scope);
    compositePermissionCreateView.setIsVisible(isVisible);
    compositePermissionCreateView.setPermissions(permissions);
    return compositePermissionCreateView;
  }

  private PermissionCreateView buildPermission(String name, String machineName, Scope scope, boolean isVisible)
  {
    final PermissionCreateView authRoleCreateView = entityViewManager.create(PermissionCreateView.class);
    authRoleCreateView.setName(name);
    authRoleCreateView.setMachineName(machineName);
    authRoleCreateView.setScope(scope);
    authRoleCreateView.setIsVisible(isVisible);
    return authRoleCreateView;
  }

  private AuthRoleCreateView buildAuthRole(String name, String machineName, Scope scope, boolean isVisible)
  {
    final AuthRoleCreateView authRoleCreateView = entityViewManager.create(AuthRoleCreateView.class);
    authRoleCreateView.setName(name);
    authRoleCreateView.setMachineName(machineName);
    authRoleCreateView.setScope(scope);
    authRoleCreateView.setIsVisible(isVisible);
    return authRoleCreateView;
  }

  @Deprecated
  private <P extends BasePermission> P persist(P permission)
  {
    return basePermissionRepository.saveAndFlush(permission);
  }

  @Deprecated
  private <R extends AuthRole> R persist(R role)
  {
    return authRoleRepository.saveAndFlush(role);
  }
}