package de.kontextwork.poc.spring.many2many.pk.domain;

import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
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
 * this parent builds a many to may relation to the child using the childs primary key
 * child_some_id
 *
 * Hint: the fields are custom named by design to properly show the mapping without being
 * ambivalent
 */
@Entity
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Data
public class ParentPkBased {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "parent_id")
  Long parentId;
  @ManyToMany(cascade = CascadeType.ALL)
  @JoinTable(
      name = "join_table_parent_pk_based",
      joinColumns = @JoinColumn(name = "myparent_id", referencedColumnName = "parent_id"),
      inverseJoinColumns = @JoinColumn(name = "mychild_id", referencedColumnName = "child_some_id")
  )
  Set<ChildPkBased> children;
}
