package de.kontextwork.poc.spring.blaze.subject.model.jpa;

import javax.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

@Data
@RequiredArgsConstructor
@Accessors(chain = true)
@Entity(name = "kontextwork_role")
public class Role
{
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private final String name;
}
