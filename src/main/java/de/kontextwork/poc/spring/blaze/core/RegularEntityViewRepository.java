package de.kontextwork.poc.spring.blaze.core;

import com.blazebit.persistence.CriteriaBuilder;
import com.blazebit.persistence.CriteriaBuilderFactory;
import com.blazebit.persistence.view.*;
import java.util.*;
import javax.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.modelmapper.internal.util.Assert;
import org.springframework.stereotype.Component;

/**
 * Implements a repository queries supporting different {@link EntityView} and predicates by injecting your own
 * {@link CriteriaBuilder} preset predicates.
 *
 * See {@link PageableEntityViewRepository} if you need pagination
 *
 * @author Eugen Mayer
 */
@Component
@RequiredArgsConstructor
@SuppressWarnings({"squid:S00119", "unused"})
public class RegularEntityViewRepository<E, ID>
{
  private final EntityManager entityManager;
  private final EntityViewManager entityViewManager;
  private final CriteriaBuilderFactory criteriaBuilderFactory;
  private final EntityViewDtoConverter entityViewDtoConverter;

  /**
   * @param id Id of your entity
   * @param setting Preconfigured {@link EntityViewSetting}
   */
  public <V> Optional<V> getOne(
    EntityViewSetting<V, CriteriaBuilder<V>> setting,
    ID id
  )
  {
    return Optional.ofNullable(entityViewManager.find(entityManager, setting, id));
  }

  /**
   * Get one by the id and pick the representation using the {@link EntityViewSetting}.
   *
   * @param setting Preconfigured {@link EntityViewSetting} for filters and representation via {@link EntityView}
   * @param entityClass Class representing the root entity
   */
  public <V> Optional<V> getOne(
    EntityViewSetting<V, CriteriaBuilder<V>> setting,
    Class<E> entityClass
  )
  {
    CriteriaBuilder<V> entityViewCriteriaBuilder = entityViewManager.applySetting(
      setting,
      entityCriteriaBuilder(entityClass)
    );
    return entityViewCriteriaBuilder.setMaxResults(1).getResultList().stream().findFirst();
  }

  /**
   * Get one using the {@link CriteriaBuilder} by predicates, pick the representation using {@link EntityViewSetting}.
   *
   * @param setting Preconfigured {@link EntityViewSetting} for filters and representation via {@link EntityView}
   * @param entityCriteriaBuilder Preconfigured {@link CriteriaBuilder}
   * @param <V> the {@link EntityView}
   */
  public <V> Optional<V> getOne(
    EntityViewSetting<V, CriteriaBuilder<V>> setting,
    CriteriaBuilder<E> entityCriteriaBuilder
  )
  {
    CriteriaBuilder<V> entityViewCriteriaBuilder = entityViewManager.applySetting(
      setting,
      entityCriteriaBuilder
    );
    return entityViewCriteriaBuilder.setMaxResults(1).getResultList().stream().findFirst();
  }

  /**
   * Find all entities matching your {@link EntityViewSetting} attribute filters, represented by the
   * {@link EntityViewSetting} {@link EntityView}.
   *
   * @param setting Preconfigured {@link EntityViewSetting} for filters and representation via {@link EntityView}
   * @param entityClass Class representing the root entity, e.g. User, Group, Space
   */
  public <V> Set<V> findAll(
    EntityViewSetting<V, CriteriaBuilder<V>> setting,
    Class<E> entityClass
  )
  {
    CriteriaBuilder<V> entityViewCriteriaBuilder = entityViewManager.applySetting(
      setting,
      entityCriteriaBuilder(entityClass)
    );
    return new HashSet<>(entityViewCriteriaBuilder.getResultList());
  }

  /**
   * Find all using the {@link CriteriaBuilder} by predicates, pick the representation using {@link EntityViewSetting}.
   *
   * @param setting Preconfigured {@link EntityViewSetting} for filters and representation via {@link EntityView}
   * @param entityCriteriaBuilder Preconfigured {@link CriteriaBuilder}
   */
  public <V> Set<V> findAll(
    EntityViewSetting<V, CriteriaBuilder<V>> setting,
    CriteriaBuilder<E> entityCriteriaBuilder
  )
  {
    Assert.notNull(setting.getEntityViewClass().getAnnotation(EntityView.class));
    CriteriaBuilder<V> entityViewCriteriaBuilder = entityViewManager.applySetting(setting, entityCriteriaBuilder);
    return new HashSet<>(entityViewCriteriaBuilder.getResultList());
  }


  /**
   * Creates a {@link Collection} of given {@code createViews}.
   *
   * @param createViews {@link Collection} of {@code createViews}
   * @param createViewClass Class of {@code createViews}
   */
  public <CV> Collection<CV> create(final Collection<CV> createViews, final Class<CV> createViewClass)
  {
    createViews.forEach(createView -> create(createView, createViewClass));
    return createViews;
  }

  public <CV> CV create(CV createView, Class<CV> createViewClass)
  {
    // if a DTO us passed in as CreateView, which can happens since a DTO implements the CreateView interface
    // it will miss the proper `EntityViewProxy` interface and EVM will fail processing it - so qualify it first
    CV qualifiedCreateView = entityViewDtoConverter.qualifyEntityCreateView(createView, createViewClass);
    entityViewManager.save(entityManager, qualifiedCreateView);
    return qualifiedCreateView;
  }

  public <ID, UV> void update(ID id, UV updateView, Class<UV> updateViewClass)
  {
    // if a DTO us passed in as CreateView, which can happens since a DTO implements the CreateView interface
    // it will miss the proper `EntityViewProxy` interface and EVM will fail processing it - so qualify it first
    UV qualifiedUpdateView = entityViewDtoConverter.qualifyEntityUpdateView(id, updateView, updateViewClass);
    entityViewManager.save(entityManager, qualifiedUpdateView);
  }

  /**
   * Updates using saveFull. Especially if you are saving empty sets one can enforce an non "lazy" aka partial mode
   * which might not run deletes on the collection this set is referencing since "the set is empty"
   */
  public <ID, UV> void updateFull(ID id, UV updateView, Class<UV> updateViewClass)
  {
    // if a DTO us passed in as CreateView, which can happens since a DTO implements the CreateView interface
    // it will miss the proper `EntityViewProxy` interface and EVM will fail processing it - so qualify it first
    UV qualifiedUpdateView = entityViewDtoConverter.qualifyEntityUpdateView(id, updateView, updateViewClass);
    entityViewManager.saveFull(entityManager, qualifiedUpdateView);
  }

  public <ID, V> void delete(ID id, Class<V> idViewClass)
  {
    // due to the way EVM works with the L1 cache we should flush it out ( whatever there is ) and clear the L1 cache
    // so nothing relies on the "L1 cache" or rather the L1 cache is empty after deleting and everything needs to be
    // refetched
    entityManager.flush();
    entityManager.clear();
    entityViewManager.remove(entityManager, idViewClass, id);
  }

  /**
   * Returns default {@link CriteriaBuilder} for {@code entityClass}.
   *
   * @param entityClass Class representing the root entity
   */
  public CriteriaBuilder<E> entityCriteriaBuilder(Class<E> entityClass)
  {
    return criteriaBuilderFactory.create(entityManager, entityClass);
  }
}
