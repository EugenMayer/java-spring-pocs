package de.kontextwork.poc.spring.many2many.nonpkservice.domain;

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
import lombok.NoArgsConstructor;

/**
 * this parent builds a many to may relation to the child using the child's non-primary (but unique)
 * key `machine`
 *
 * Since reading does not work for this relation as we see in the `nonpk` moe3l we will use a
 * service to read / fill the children but still use the jointable m2m relation for all the writing
 * and updating
 */
@Entity
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Data
public class ParentNonPkServiceBased {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "parent_id")
  Long parentId;
  // this is actually not build to offer readability, but rather "saveability"
  // the former wont work, so we will use a service to load it instead
  // but use a join-table m2m relation when writing (this actually works
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
      name = "join_table_parent_non_pk_service_based",
      // name -> col field name in the join table
      joinColumns = @JoinColumn(name = "myparent_id", referencedColumnName = "parent_id"),
      inverseJoinColumns = @JoinColumn(name = "mychild_machine", referencedColumnName = "machine", unique = true, nullable = false)
  )
  Set<ChildNonPkServiceBased> children;
}
