package de.kontextwork.poc.spring.blaze.subject.model.jpa;

import java.util.*;
import javax.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;

@Data
@Entity
@Accessors(chain = true)
@RequiredArgsConstructor
@DiscriminatorValue("GROUP")
@Table(name = "kontextwork_group")
@EqualsAndHashCode(callSuper = true)
public class Group extends Subject
{
  private final String name;

  @OneToMany(fetch = FetchType.LAZY)
  private final Set<User> members;
}
