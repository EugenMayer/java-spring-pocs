package de.kontextwork.poc.spring.blaze.pageable.model.jpa;

import javax.persistence.*;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Entity
@Accessors(chain = true)
@Table(name = "blaze_persistence_cat")
public class Cat
{
  @Id
  private Long id;
  private String name;
  private String implantCode;
  private String color;
}
