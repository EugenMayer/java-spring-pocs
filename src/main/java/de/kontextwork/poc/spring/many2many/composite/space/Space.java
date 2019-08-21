package de.kontextwork.poc.spring.many2many.composite.space;

import javax.persistence.*;
import javax.persistence.Entity;
import lombok.*;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "na_space")
public class Space
{
  @Id
  @Column(length = 10)
  private String name;
}