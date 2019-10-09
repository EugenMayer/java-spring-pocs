package de.kontextwork.poc.spring.blaze.pageable.model.domain;

import com.blazebit.persistence.view.EntityView;
import de.kontextwork.poc.spring.blaze.pageable.model.jpa.Cat;

@EntityView(Cat.class)
public interface CatExcerptView extends CatIdView
{
  String getName();
}
