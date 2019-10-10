package de.kontextwork.poc.spring.blaze.core;

import com.blazebit.persistence.PagedList;
import org.springframework.data.domain.*;

/**
 * Basic {@code Page} implementation adopting paged results from provided {@link PagedList}.
 *
 * @author Sebastian Ullrich
 */
public class EntityViewPage<T> extends PageImpl<T>
{
  /**
   * Constructor adopting results of {@link PagedList}.
   *
   * @param pagedList paged results
   * @param pageable provided paging restrictions
   */
  private EntityViewPage(PagedList<T> pagedList, Pageable pageable)
  {
    super(pagedList, pageable, pagedList.getTotalSize());
  }

  /**
   * Static convenience constructor adopting results of {@link PagedList}.
   *
   * @param pagedList paged results
   * @param pageable provided paging restrictions
   */
  public static <T> EntityViewPage<T> of(PagedList<T> pagedList, Pageable pageable)
  {
    return new EntityViewPage<>(pagedList, pageable);
  }
}
