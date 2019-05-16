package de.kontextwork.poc.spring.many2many.inheritance.domain;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NaturalId;

/**
 * this parent builds a many to may relation to the child using the child's non-primary (but unique)
 * key `machine`
 * In addition / difference to the others, the relation is build not only through a non-pk on the child
 * but this time also a non-pk on the parent
 *
 * Since reading does not work for this relation as we see in the `nonpk` moe3l we will use a
 * service to read / fill the children but still use the jointable m2m relation for all the writing
 * and updating
 *
 *
 */
@Entity
@Table(name = "inheritance_base_type")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(
    name = "discriminator",
    discriminatorType = DiscriminatorType.STRING,
    length = 32
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
// we need `implements Serializable` once again as with BaseType since for non-pk relations this is mandatory
public class BaseType {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  Long id;

  // HINT: even though semantically it belongs here, defining this in the base-type would let us now longer
  // be able to create a joinTable, since JPA cannot distinguish between the different machine of child and parent
  // and gives us an error of a "field not mapped to a single entity" error
  // moving this field to the subtypes fixes that
  //  @EqualsAndHashCode.Include
  //  @Column(columnDefinition = "VARCHAR(200)", unique = true)
  //  protected String machine;
}
