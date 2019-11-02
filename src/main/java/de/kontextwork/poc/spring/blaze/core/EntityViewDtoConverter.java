package de.kontextwork.poc.spring.blaze.core;

import com.blazebit.persistence.view.EntityViewManager;
import com.blazebit.persistence.view.spi.type.EntityViewProxy;
import javax.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EntityViewDtoConverter
{
  private final EntityViewManager entityViewManager;
  private final ModelMapper modelMapper;

  /**
   * Takes your EntityCreateView instance and qualifies it in a way so it can be used with
   * {@link EntityViewManager#save(EntityManager, Object)} which implies that the instance implements
   * {@link EntityViewProxy}. If the instance is one of our DTOs implementing the EntityCreateView of choice, it will
   * not implement {@link EntityViewProxy} properly - for that we map the DTO to a proper skeleton created by
   * {@link EntityViewManager#create(Class)} including the {@link EntityViewProxy}
   *
   * @param createViewOrDto an instance of your EntityCreateView
   * @param entityCreateViewClass your target EntityCreateView class
   * @param <V> Your target create view type like UserCreateView
   */
  public <V> V qualifyEntityCreateView(V createViewOrDto, Class<V> entityCreateViewClass)
  {
    if (!(createViewOrDto instanceof EntityViewProxy)) {
      // we might get a DTO as groupCreateView (and not a real GroupCreateView with all the proxies applied to it
      // so missing fields like like "isNews" .. so just map it into a real
      // thus we create a fresh one and map it using the model mapper
      final V targetCreateView = entityViewManager.create(entityCreateViewClass);

      // we need to ensure this so we do not override defaults of the view with null fields from our input
      modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
      modelMapper.map(createViewOrDto, targetCreateView);
      return targetCreateView;
    }
    else {
      return createViewOrDto;
    }
  }

  /**
   * Takes your EntityUpdateView instance and qualifies it in a way so it can be used with
   * {@link EntityViewManager#save(EntityManager, Object)} which implies that the instance implements
   * {@link EntityViewProxy}. If the instance is one of our DTOs implementing the EntityUpdateView of choice, it will
   * not implement {@link EntityViewProxy} properly - for that we map the DTO to a proper skeleton created by
   * {@link EntityViewManager#getReference(Class, Object)} (Class)} including the {@link EntityViewProxy}
   * Also @see EntityViewDtoConverter#qualifyEntityUpdateView
   *
   * @param id the id of the entity you will be updating
   * @param updateViewOrDto an instance of your EntityCreateView
   * @param entityUpdateViewClass your target EntityCreateView class
   * @param <V> Your target create view type like UserCreateView
   * @param <ID> The id class of your entity, e.g. Long or Integer
   */
  public <V, ID> V qualifyEntityUpdateView(ID id, V updateViewOrDto, Class<V> entityUpdateViewClass)
  {
    if (!(updateViewOrDto instanceof EntityViewProxy)) {
      // We might get a DTO as groupUpdateView (and not a real GroupUpdateView with all the proxies applied to it
      // so missing fields like like "isNews" .. so just map it into a real
      // thus we create a fresh one and map it using the model mapper
      final V targetCreateView = entityViewManager.getReference(entityUpdateViewClass, id);

      // we need to ensure this so we do not override defaults of the view with null fields from our input
      modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
      modelMapper.map(updateViewOrDto, targetCreateView);
      return targetCreateView;
    }
    else {
      return updateViewOrDto;
    }
  }
}
