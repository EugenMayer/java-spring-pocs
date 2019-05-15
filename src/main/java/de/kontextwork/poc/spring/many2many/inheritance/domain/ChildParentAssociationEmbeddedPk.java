package de.kontextwork.poc.spring.many2many.inheritance.domain;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Embeddable
class ChildParentAssociationEmbeddedPk implements Serializable {
  @Column(name = "myparent_machine", columnDefinition = "VARCHAR(100)", nullable = false)
  private String parentMachine;
  @Column(name = "mychild_machine", columnDefinition = "VARCHAR(100)", nullable = false)
  private String childMachine;

  public ChildParentAssociationEmbeddedPk(final String parentMachine, final String childMachine) {
    this.parentMachine = parentMachine;
    this.childMachine = childMachine;
  }
}