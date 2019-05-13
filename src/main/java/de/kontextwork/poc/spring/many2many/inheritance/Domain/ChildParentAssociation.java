package de.kontextwork.poc.spring.many2many.inheritance.Domain;

import java.io.Serializable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * When using inheritance with single-table, we cannot use the normal
 * many to many relation, since we get an exception  like
 *
 * org.hibernate.AnnotationException: referencedColumnNames(machine)
 * of de.kontextwork.poc.spring.many2many.bothnonpkserviceself.Domain.ChildBothNonPkServiceBasedSelf.children referencing
 * de.kontextwork.poc.spring.many2many.bothnonpkserviceself.Domain.ParentBothNonPkServiceBasedSelf not mapped to a single property
 *
 * we have to either switch to a @MappedSuperclass - so table per type, or we have to use
 * this custom association table - since we want to a single table implemenation of the inheritance
 */
@Entity
@Table(name="join_table_inheritance")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Data
public class ChildParentAssociation {
  @EmbeddedId
  ChildParentAssociationEmbeddedPk id;

  public ChildParentAssociation(final String parentMachine, final String childMachine) {
    this.id = new ChildParentAssociationEmbeddedPk(parentMachine, childMachine);
  }
}
