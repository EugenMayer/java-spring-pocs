package de.kontextwork.poc.spring.blaze.core;

import com.blazebit.persistence.*;
import com.blazebit.persistence.view.*;
import java.util.Collection;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.modelmapper.internal.util.Assert;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

/**
 * Repository implementation that adopts Spring Pagination with BlazePersistence Entity View querying.
 *
 * @author Sebastian Ullrich
 */
@Component
@RequiredArgsConstructor
@SuppressWarnings({"unused"})
public class PageableEntityViewRepository<E>
{
  private final EntityManager entityManager;
  private final EntityViewManager entityViewManager;
  private final CriteriaBuilderFactory criteriaBuilderFactory;

  /**
   * Returns a {@link EntityViewPage} of entity views meeting the paging restriction provided in the
   * {@link Pageable} object.
   *
   * @param entityClass Class representing the root entity
   * @param setting Preconfigured {@link EntityViewSetting}
   * @param pageable Provided paging restrictions
   */
  public <V> Page<V> findAll(
    Class<E> entityClass,
    EntityViewSetting<V, PaginatedCriteriaBuilder<V>> setting,
    Pageable pageable
  )
  {
    return findAll(setting, defaultCriteriaBuilder(entityClass), pageable);
  }

  /**
   * Returns a {@link EntityViewPage} of entity views meeting the paging restriction provided in the
   * {@link Pageable} object.
   *
   * @param setting Preconfigured {@link EntityViewSetting}
   * @param criteriaBuilder Preconfigured {@link CriteriaBuilder}
   * @param pageable Provided paging restrictions
   */
  public <V> Page<V> findAll(
    EntityViewSetting<V, PaginatedCriteriaBuilder<V>> setting,
    CriteriaBuilder<E> criteriaBuilder,
    Pageable pageable
  )
  {
    Assert.notNull(criteriaBuilder.getResultType().getAnnotation(Entity.class));
    Assert.notNull(setting.getEntityViewClass().getAnnotation(EntityView.class));

    return EntityViewPage.of(entityViewManager.applySetting(setting, criteriaBuilder).getResultList(), pageable);
  }

  /**
   * Returns default {@link CriteriaBuilder} for {@code entityClass}.
   *
   * @param entityClass Class representing the root entity
   */
  private CriteriaBuilder<E> defaultCriteriaBuilder(Class<E> entityClass)
  {
    return criteriaBuilderFactory.create(entityManager, entityClass);
  }
}
