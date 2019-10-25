package de.kontextwork.poc.spring.blaze.subject.model.jpa.subject;

import de.kontextwork.poc.spring.blaze.subject.model.jpa.User;
import java.util.*;
import javax.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;

/**
 * We have to use an alias here since 'Group' is a reserved keyword in JPQL/HQL.
 * This leads to issues in JPQL TREAT. Renaming the table name is insufficient
 * here since 'Group' is a valid table name here.
 */
@Entity(name = "SubjectGroup")

@Data
@Accessors(chain = true)
@DiscriminatorValue("1")
@Table(name = "subject_group")
@EqualsAndHashCode(callSuper = true)
public class Group extends Subject
{
  private String name;

  @ManyToMany(fetch = FetchType.LAZY)
  private Set<User> members;

  public Group(final String name, final Set<User> members)
  {
    this.name = name;
    this.members = members;
  }
}