package de.kontextwork.poc.spring.many2many.inheritance.domain;

import de.kontextwork.poc.spring.many2many.inheritance.domain.base.BaseType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

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
@DiscriminatorValue("CHILD")
@Data
@EqualsAndHashCode(callSuper = true)
// we need `implements Serializable` once again as with ChildNonPk since for non-pk relations this is mandatory
public class ChildInheritanceBased extends BaseType {
  public ChildInheritanceBased(final String machine) {
    super(machine);
  }

  protected ChildInheritanceBased() {
    super();
  }
}
