package de.kontextwork.poc.spring.many2many.nonpk.domain;

import java.io.Serializable;
import javax.persistence.*;
import lombok.*;
import org.hibernate.annotations.NaturalId;

@SuppressWarnings("unused")
@Entity
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Data
// Indeed we need 'implements Serializable' when using the natural key for a relation
// Interestingly this does fail when we read data, not when we write
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ChildNonPk implements Serializable
{
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "child_some_id")
  @EqualsAndHashCode.Include
  Long someId;
  // defining columnDefinition = "VARCHAR(200)"
  // is mandatory or we get a "key to large" error
  @NaturalId
  @EqualsAndHashCode.Include
  @Column(columnDefinition = "VARCHAR(200)", unique = true)
  String machine;

  public ChildNonPk(final String machine)
  {
    this.machine = machine;
  }
}
