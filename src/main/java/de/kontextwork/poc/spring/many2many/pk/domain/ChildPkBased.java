package de.kontextwork.poc.spring.many2many.pk.domain;

import javax.persistence.*;
import lombok.*;

@SuppressWarnings("unused")
@Entity
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Data
public class ChildPkBased
{
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "child_some_id")
  Long someId;
  // we need at least one field our children can differ after being creating
  // through the constructur, otherwise when creating to children and saving them
  // through the parent, we end up having only one children since they have been "equal" in the set
  String name;

  public ChildPkBased(final String name)
  {
    this.name = name;
  }
}
