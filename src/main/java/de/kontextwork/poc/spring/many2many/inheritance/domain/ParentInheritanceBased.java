package de.kontextwork.poc.spring.many2many.inheritance.domain;

import de.kontextwork.poc.spring.many2many.inheritance.domain.base.BaseType;
import java.util.Set;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;
import lombok.Data;

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
@DiscriminatorValue("PARENT")
@Data
// we need `implements Serializable` once again as with ChildNonPk since for non-pk relations this is mandatory
public class ParentInheritanceBased extends BaseType {
  // this is actually not build to offer readability, but rather "saveability"
  // the former wont work, so we will use a service to load it instead
  // but use a join-table m2m relation when writing (this actually works
  @Transient
  Set<ChildInheritanceBased> children;

  public ParentInheritanceBased(final String machine) {
    super(machine);
  }

  protected ParentInheritanceBased() {
    super();
  }
}
