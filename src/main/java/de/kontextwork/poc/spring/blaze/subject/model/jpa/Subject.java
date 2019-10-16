package de.kontextwork.poc.spring.blaze.subject.model.jpa;

import javax.persistence.*;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@DiscriminatorColumn(name = "TYPE")
@Entity(name = "kontextwork_subject")
@Inheritance(strategy = InheritanceType.JOINED)
public class Subject
{
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
}
