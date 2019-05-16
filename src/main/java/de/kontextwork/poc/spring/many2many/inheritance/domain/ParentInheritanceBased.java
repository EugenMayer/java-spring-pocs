package de.kontextwork.poc.spring.many2many.inheritance.domain;

import java.io.Serializable;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * this parent builds a many to may relation to the child using the child's non-primary (but unique)
 * key `machine` In addition / difference to the others, the relation is build not only through a
 * non-pk on the child but this time also a non-pk on the parent
 *
 * Since reading does not work for this relation as we see in the `nonpk` moe3l we will use a
 * service to read / fill the children but still use the jointable m2m relation for all the writing
 * and updating
 */
@Entity
@DiscriminatorValue("PARENT")
@Data
@EqualsAndHashCode(callSuper = true)
// we need `implements Serializable` once again as with ChildNonPk since for non-pk relations this is mandatory
public class ParentInheritanceBased extends BaseType implements Serializable {
  // HINT: this is the important part - instead of defining that in BaseType - we have to move it
  // to our subtypes so that JPA can later understand the different "machine" fields in the joinTable
  @EqualsAndHashCode.Include
  @Column(columnDefinition = "VARCHAR(200)", unique = true)
  protected String machine;
  // this is actually not build to offer readability, but rather "saveability"
  // the former wont work, so we will use a service to load it instead
  // but use a join-table m2m relation when writing (this actually works
  //@Transient
  @ManyToMany(
      cascade = {
          CascadeType.DETACH,
          CascadeType.MERGE,
          CascadeType.PERSIST,
          CascadeType.REMOVE
      },
      fetch = FetchType.LAZY
  )
  @JoinTable(
      name = "join_table_inheritance",
      joinColumns = @JoinColumn(name = "myparent_machine", referencedColumnName = "machine"),
      inverseJoinColumns = @JoinColumn(name = "mychild_machine", referencedColumnName = "machine")
  )
  Set<ChildInheritanceBased> children;

  public ParentInheritanceBased(final String machine) {
    super();
    this.machine = machine;
  }

  @SuppressWarnings("unused")
  protected ParentInheritanceBased() {
    super();
  }
}
