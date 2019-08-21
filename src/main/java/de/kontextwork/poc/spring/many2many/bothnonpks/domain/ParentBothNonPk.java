package de.kontextwork.poc.spring.many2many.bothnonpks.domain;

import java.io.Serializable;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NaturalId;

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
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
// we need `implements Serializable` once again as with ChildNonPk since for non-pk relations this is mandatory
public class ParentBothNonPk implements Serializable {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "parent_id")
  Long parentId;
  // defining columnDefinition = "VARCHAR(200)"
  // is mandatory or we get a "key to large" error
  @NaturalId
  @EqualsAndHashCode.Include
  @Column(columnDefinition = "VARCHAR(200)", unique = true)
  String machine;

  // Hint: it is important to exclude CascadeType.REFRESH since we cannot do this in this case
  @ManyToMany(
      cascade = {
          CascadeType.DETACH,
          CascadeType.MERGE,
          CascadeType.PERSIST,
          CascadeType.REMOVE
      }
      , fetch = FetchType.LAZY
  )
  @JoinTable(
      // not using our usualy names, since they are too lon
      name = "join_table_parent_both_non_pk",
      // VARCHAR(120) since we have a max auf 3072 for the index
      joinColumns = @JoinColumn(name = "myparent_machine", referencedColumnName = "machine", columnDefinition = "VARCHAR(120)"),
      inverseJoinColumns = @JoinColumn(name = "mychild_machine", referencedColumnName = "machine", columnDefinition = "VARCHAR(120)")
  )
  Set<ChildBothNonPk> children;

  public ParentBothNonPk(final String machine) {
    this.machine = machine;
  }
}
