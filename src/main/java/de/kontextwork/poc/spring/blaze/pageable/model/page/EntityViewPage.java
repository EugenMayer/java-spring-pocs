package de.kontextwork.poc.spring.blaze.pageable.model.page;

import com.blazebit.persistence.PagedList;
import org.springframework.data.domain.*;

/**
 *
 * @param <T>
 */
public class EntityViewPage<T> extends PageImpl<T>
{
  private EntityViewPage(PagedList<T> pagedList, Pageable pageable)
  {
    super(pagedList, pageable, pagedList.getTotalSize());
  }

  public static <T> EntityViewPage<T> of(PagedList<T> pagedList, Pageable pageable)
  {
    return new EntityViewPage<>(pagedList, pageable);
  }
}
