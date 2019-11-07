package de.kontextwork.poc.spring.blaze.fullapp.subject.group.model.jpa;

import de.kontextwork.poc.spring.blaze.fullapp.subject.model.jpa.Subject;
import de.kontextwork.poc.spring.blaze.fullapp.subject.user.model.jpa.User;
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
@NoArgsConstructor
public class Group extends Subject
{
  private String name;

  @ManyToMany(fetch = FetchType.LAZY)
  private Set<User> members;

  // We have to rename the actual field name due to https://github.com/Blazebit/blaze-persistence/issues/909
  @Column(name="machine")
  private String machineMachine;

  public Group(final String name, final Set<User> members)
  {
    this.name = name;
    this.members = members;
    this.machineMachine = UUID.randomUUID().toString();
  }
}
