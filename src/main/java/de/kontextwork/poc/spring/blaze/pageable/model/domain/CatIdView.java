package de.kontextwork.poc.spring.blaze.pageable.model.domain;

import com.blazebit.persistence.view.*;
import de.kontextwork.poc.spring.blaze.pageable.model.jpa.Cat;

@EntityView(Cat.class)
public interface CatIdView
{
  @IdMapping
  Long getId();
}
