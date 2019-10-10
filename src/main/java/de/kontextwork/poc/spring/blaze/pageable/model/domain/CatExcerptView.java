package de.kontextwork.poc.spring.blaze.pageable.model.domain;

import com.blazebit.persistence.view.*;
import com.blazebit.persistence.view.filter.EqualFilter;
import de.kontextwork.poc.spring.blaze.pageable.model.jpa.Cat;

@EntityView(Cat.class)
public interface CatExcerptView extends CatIdView
{
  @AttributeFilters({
    @AttributeFilter(value = EqualFilter.class),
  })
  String getName();

  @AttributeFilters({
    @AttributeFilter(value = EqualFilter.class),
  })
  String getColor();
}
