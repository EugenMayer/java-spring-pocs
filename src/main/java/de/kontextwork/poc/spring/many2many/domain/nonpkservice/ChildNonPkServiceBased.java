package de.kontextwork.poc.spring.many2many.domain.nonpkservice;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NaturalId;

@SuppressWarnings("unused")
@Entity
@Table(name = "child_non_pk_service_based")
@NoArgsConstructor
@Data
// Indeed we need 'implements Serializable' when using the natural key for a relation
// Interestingly this does fail when we read data, not when we write
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ChildNonPkServiceBased implements Serializable {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name="child_some_id")
  @EqualsAndHashCode.Include
  Long someId;

  // defining columnDefinition = "VARCHAR(200)"
  // is mandatory or we get a "key to large" error
  @NaturalId
  @EqualsAndHashCode.Include
  @Column(columnDefinition = "VARCHAR(200)", unique = true)
  String machine;
  public ChildNonPkServiceBased(final String machine) {
    this.machine = machine;
  }
}