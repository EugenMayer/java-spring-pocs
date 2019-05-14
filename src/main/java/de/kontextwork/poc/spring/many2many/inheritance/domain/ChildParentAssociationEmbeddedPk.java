package de.kontextwork.poc.spring.many2many.inheritance.domain;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
class ChildParentAssociationEmbeddedPk implements Serializable {
  @Column(name = "myparent_machine", columnDefinition = "VARCHAR(100)", nullable = false)
  String parentMachine;
  @Column(name = "mychild_machine", columnDefinition = "VARCHAR(100)", nullable = false)
  String childMachine;

  public ChildParentAssociationEmbeddedPk() {
  }

  public ChildParentAssociationEmbeddedPk(final String parentMachine, final String childMachine) {
    this.parentMachine = parentMachine;
    this.childMachine = childMachine;
  }
}