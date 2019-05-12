package de.kontextwork.poc.spring.many2many.nonpk.domain;

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
import lombok.Data;

/**
 * this parent builds a many to may relation to the child using the child's non-primary (but unique)
 * key `machine`
 *
 * Hint: the fields are custom named by design to properly show the mapping without being ambivalent
 * or use auto-naming - this ensure clarity about what is where
 */
@Entity
@Data
public class ParentNonPkBased {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "parent_id")
  Long parentId;
  @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  @JoinTable(
      name = "join_table_parent_non_pk_based",
      // name -> col field name in the join table
      joinColumns = @JoinColumn(name = "myparent_id", referencedColumnName = "parent_id"),
      inverseJoinColumns = @JoinColumn(name = "mychild_machine", referencedColumnName = "machine", unique = true, nullable = false)
  )
  Set<ChildNonPkBased> children;
}
