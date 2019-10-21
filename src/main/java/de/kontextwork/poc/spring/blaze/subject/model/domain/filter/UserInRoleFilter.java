package de.kontextwork.poc.spring.blaze.subject.model.domain.filter;

import de.kontextwork.poc.spring.blaze.subject.model.jpa.member.GlobalRoleMember;
import java.util.Set;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * This class must have public access in order to be accessible for BP.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserInRoleFilter
{
  private static final String PATH_TO_ROLE = "roleMember";
  private static final String PATH_TO_ROLE_VIA_GROUP = "groups.roleMember";

  /**
   * View Filter to check on {@link GlobalRoleMember} for {@code ROLE_ADMINISTRATOR}.
   */
  public static class Administrator extends RoleFilter
  {
    public static final String NAME = "GLOBAL_ROLE_MEMBER_FILTER_ROLE_ADMINISTRATOR";

    public Administrator()
    {
      super("ROLE_ADMINISTRATOR", GlobalRoleMember.class, Set.of(PATH_TO_ROLE, PATH_TO_ROLE_VIA_GROUP));
    }
  }

  /**
   * View Filter to check on {@link GlobalRoleMember} for {@code ROLE_MODERATOR}.
   */
  public static class Moderator extends RoleFilter
  {
    public static final String NAME = "GLOBAL_ROLE_MEMBER_FILTER_ROLE_MODERATOR";

    public Moderator()
    {
      super("ROLE_MODERATOR", GlobalRoleMember.class, Set.of(PATH_TO_ROLE, PATH_TO_ROLE_VIA_GROUP));
    }
  }

  /**
   * View Filter to check on {@link GlobalRoleMember} for {@code ROLE_USER}.
   */
  public static class User extends RoleFilter
  {
    public static final String NAME = "GLOBAL_ROLE_MEMBER_FILTER_ROLE_USER";

    public User()
    {
      super("ROLE_USER", GlobalRoleMember.class, Set.of(PATH_TO_ROLE, PATH_TO_ROLE_VIA_GROUP));
    }
  }
}
