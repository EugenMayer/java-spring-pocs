package de.kontextwork.poc.spring.many2many.pk.domain;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.config.Projection;

@Projection(
  name = "ParentPkBasedDTO",
  types = {ParentPkBased.class})
public interface ParentPkBasedDTO
{
  Long getParentId();

  @Value("#{target.children.size()}")
  Integer getChildCount();
}
