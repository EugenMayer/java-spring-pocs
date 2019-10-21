package de.kontextwork.poc.spring.blaze.core;

import javax.persistence.Entity;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/**
 * Utility class for JPA expressions.
 *
 * @author Sebastian Ullrich
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JpaExpressionHelper
{
  /**
   * Constructs a JPQL {@code TYPE} check expression.
   *
   * @param path Path of the source entity
   * @param entity Target entity
   * @return JPQL {@code TYPE} check expression
   */
  public static String typeCheck(String path, Object entity)
  {
    Assert.notNull(entity, "Provided entity is null!");
    return typeCheck(path, entity.getClass());
  }

  /**
   * Constructs a JPQL {@code TYPE} check expression.
   *
   * @param path Path of the source entity
   * @param entityClass Class of the target entity
   * @return JPQL {@code TYPE} check expression
   */
  public static String typeCheck(String path, Class entityClass)
  {
    Assert.isTrue(!StringUtils.isEmpty(path), "Provided path is empty!");
    Assert.notNull(entityClass, "Provided entityClass is null!");
    Assert.notNull(entityClass.getAnnotation(Entity.class), "Provided entityClass is not annotated with @Entity!");

    return String.format("TYPE(%s) = %s", path, entityClass.getSimpleName());
  }
}
