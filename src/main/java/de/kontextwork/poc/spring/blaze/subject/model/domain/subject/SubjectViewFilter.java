package de.kontextwork.poc.spring.blaze.subject.model.domain.subject;

import com.blazebit.persistence.WhereBuilder;
import com.blazebit.persistence.view.ViewFilterProvider;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * This class must have public access in order to be accessible for BP.
 */
@SuppressWarnings("WeakerAccess")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SubjectViewFilter
{
  private static final String ROLE_NAME_PATH = "roleMember.role.name";

  public static class UserRoleFilterProvider extends ViewFilterProvider
  {
    @Override
    public <T extends WhereBuilder<T>> T apply(T whereBuilder)
    {
      return whereBuilder.where(ROLE_NAME_PATH).eq("ROLE_USER");
    }
  }

  public static class ModeratorRoleFilter extends ViewFilterProvider
  {
    @Override
    public <T extends WhereBuilder<T>> T apply(T whereBuilder)
    {
      return whereBuilder.where(ROLE_NAME_PATH).eq("ROLE_MODERATOR");
    }
  }

  public static class AdministratorRoleFilter extends ViewFilterProvider
  {
    @Override
    public <T extends WhereBuilder<T>> T apply(T whereBuilder)
    {
      return whereBuilder.where(ROLE_NAME_PATH).eq("ROLE_ADMINISTRATOR");
    }
  }
}
