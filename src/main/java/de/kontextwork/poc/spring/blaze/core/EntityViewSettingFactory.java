package de.kontextwork.poc.spring.blaze.core;

import com.blazebit.persistence.PaginatedCriteriaBuilder;
import com.blazebit.persistence.view.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.*;

/**
 * Factory class that utilizes the creation of {@link EntityViewSetting} for domain specific use cases.
 *
 * @author Sebastian Ullrich
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EntityViewSettingFactory
{
  /**
   * Creates pagination qualified {@link EntityViewSetting}.
   *
   * @param entityViewClass Class representing the result set
   * @param pageable Provided paging restrictions
   */
  public static <T> EntityViewSetting<T, PaginatedCriteriaBuilder<T>> create(
    Class<T> entityViewClass,
    Pageable pageable
  )
  {
    int firstResult = pageable.getPageNumber() * pageable.getPageSize();
    int maxResults = pageable.getPageSize();

    EntityViewSetting<T, PaginatedCriteriaBuilder<T>> setting = EntityViewSetting
      .create(entityViewClass, firstResult, maxResults);

    for (Order order : pageable.getSort()) {
      boolean nullsFirst = order.getNullHandling() == NullHandling.NULLS_FIRST;

      Sorter sorter = order.isAscending()
        ? Sorters.ascending(nullsFirst)
        : Sorters.descending(nullsFirst);

      setting.addAttributeSorter(order.getProperty(), sorter);
    }

    return setting;
  }
}
