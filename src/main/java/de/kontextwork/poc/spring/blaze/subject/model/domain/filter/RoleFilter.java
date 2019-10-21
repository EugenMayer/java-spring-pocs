package de.kontextwork.poc.spring.blaze.subject.model.domain.filter;

import com.blazebit.persistence.WhereBuilder;
import com.blazebit.persistence.WhereOrBuilder;
import com.blazebit.persistence.view.ViewFilterProvider;
import de.kontextwork.poc.spring.blaze.subject.model.jpa.member.RoleMember;
import java.util.Set;
import lombok.RequiredArgsConstructor;

import static de.kontextwork.poc.spring.blaze.core.JpaExpressionHelper.typeCheck;

@RequiredArgsConstructor
public abstract class RoleFilter extends ViewFilterProvider
{
  private final String roleName;
  private final Class<? extends RoleMember> type;
  private final Set<String> pathsToRoleMember;

  @Override
  public <T extends WhereBuilder<T>> T apply(final T whereBuilder)
  {
    final WhereOrBuilder<T> builder = whereBuilder.whereOr();

    for (String pathToRoleMember : pathsToRoleMember) {
      builder.whereAnd()
        .where(pathToRoleMember + ".role.name").eq(roleName)
        .whereExpression(typeCheck(pathToRoleMember, type))
        .endAnd();
    }

    return builder.endOr();
  }
}
