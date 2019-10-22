package de.kontextwork.poc.spring.blaze.subject.model.domain;

import com.blazebit.persistence.WhereBuilder;
import com.blazebit.persistence.WhereOrBuilder;
import com.blazebit.persistence.view.ViewFilterProvider;
import de.kontextwork.poc.spring.blaze.subject.model.jpa.member.RealmRoleMembership;
import de.kontextwork.poc.spring.blaze.subject.model.jpa.role.*;
import de.kontextwork.poc.spring.blaze.subject.model.jpa.subject.Subject;
import de.kontextwork.poc.spring.blaze.subject.model.jpa.member.GlobalRoleMembership;
import java.util.Set;
import lombok.*;

import static de.kontextwork.poc.spring.blaze.core.JpaExpressionHelper.typeCheck;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
final class SubjectFilter
{
  private static final String PATH_TO_GLOBAL_ROLE_MEMBERSHIPS = "globalRoleMembership";
  private static final String PATH_TO_GLOBAL_ROLE_MEMBERSHIPS_VIA_GROUP = "groups.globalRoleMembership";

  /**
   * Class to hold all Filters that filter {@link Subject} based on their
   */
  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  static final class GlobalRoleFilter
  {
    /**
     * View Filter to check on {@link GlobalRoleMembership} for {@code ROLE_ADMINISTRATOR}.
     */
    public static final class Administrator extends RoleMemberRoleNameFilterProvider
    {
      public Administrator()
      {
        super("ROLE_ADMINISTRATOR", GlobalRole.class,
          Set.of(PATH_TO_GLOBAL_ROLE_MEMBERSHIPS, PATH_TO_GLOBAL_ROLE_MEMBERSHIPS_VIA_GROUP));
      }
    }

    /**
     * View Filter to check on {@link GlobalRoleMembership} for {@code ROLE_MODERATOR}.
     */
    public static final class Moderator extends RoleMemberRoleNameFilterProvider
    {
      public Moderator()
      {
        super("ROLE_MODERATOR", GlobalRole.class,
          Set.of(PATH_TO_GLOBAL_ROLE_MEMBERSHIPS, PATH_TO_GLOBAL_ROLE_MEMBERSHIPS_VIA_GROUP));
      }
    }

    /**
     * View Filter to check on {@link GlobalRoleMembership} for {@code ROLE_USER}.
     */
    public static final class User extends RoleMemberRoleNameFilterProvider
    {
      public User()
      {
        super("ROLE_USER", GlobalRole.class,
          Set.of(PATH_TO_GLOBAL_ROLE_MEMBERSHIPS, PATH_TO_GLOBAL_ROLE_MEMBERSHIPS_VIA_GROUP));
      }
    }
  }

  private static final String PATH_TO_REALM_ROLE_MEMBERSHIPS = "realmRoleMembership";
  private static final String PATH_TO_REALM_ROLE_MEMBERSHIPS_VIA_GROUP = "groups.realmRoleMembership";

  /**
   * Class to hold all Filters that filter {@link Subject} based on their
   */
  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  static final class RealmRoleFilter
  {
    /**
     * View Filter to check on {@link GlobalRoleMembership} for {@code ROLE_ADMINISTRATOR}.
     */
    public static final class Administrator extends RoleMemberRoleNameFilterProvider
    {
      public Administrator()
      {
        super("ROLE_ADMINISTRATOR", RealmRole.class,
          Set.of(PATH_TO_REALM_ROLE_MEMBERSHIPS, PATH_TO_REALM_ROLE_MEMBERSHIPS_VIA_GROUP));
      }
    }

    /**
     * View Filter to check on {@link GlobalRoleMembership} for {@code ROLE_MODERATOR}.
     */
    public static final class Moderator extends RoleMemberRoleNameFilterProvider
    {
      public Moderator()
      {
        super("ROLE_MODERATOR", RealmRole.class,
          Set.of(PATH_TO_REALM_ROLE_MEMBERSHIPS, PATH_TO_REALM_ROLE_MEMBERSHIPS_VIA_GROUP));
      }
    }

    /**
     * View Filter to check on {@link GlobalRoleMembership} for {@code ROLE_USER}.
     */
    public static final class User extends RoleMemberRoleNameFilterProvider
    {
      public User()
      {
        super("ROLE_USER", RealmRole.class,
          Set.of(PATH_TO_REALM_ROLE_MEMBERSHIPS, PATH_TO_REALM_ROLE_MEMBERSHIPS_VIA_GROUP));
      }
    }
  }

  @RequiredArgsConstructor
  abstract static class RoleMemberRoleNameFilterProvider extends ViewFilterProvider
  {
    private final String roleName;
    private final Class<? extends Role> roleType;
    private final Set<String> pathsToRoleMember;

    @Override
    public <T extends WhereBuilder<T>> T apply(final T whereBuilder)
    {
      final WhereOrBuilder<T> builder = whereBuilder.whereOr();

      for (String pathToRoleMember : pathsToRoleMember) {
        builder.whereAnd()
          .whereExpression(typeCheck(pathToRoleMember + ".role", roleType))
          .where(pathToRoleMember + ".role.name").eq(roleName)
          .endAnd();
      }

      return builder.endOr();
    }
  }
}
