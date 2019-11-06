package de.kontextwork.poc.spring.blaze.fullapp.subject.user.model.jpa;

import de.kontextwork.poc.spring.blaze.fullapp.subject.group.model.jpa.Group;
import de.kontextwork.poc.spring.blaze.fullapp.subject.model.jpa.Subject;
import java.util.Set;
import java.util.UUID;
import javax.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Generated;

import static org.hibernate.annotations.GenerationTime.INSERT;

@Data
@Entity
@NoArgsConstructor
@Accessors(chain = true)
@DiscriminatorValue("0")
@Table(name = "subject_user")
@EqualsAndHashCode(callSuper = true)
public class User extends Subject
{
  /**
   * This is a legacy {@code UID} that still has some associations attached, so we need to
   * maintain this field when creating a new {@link User} (e.g. {@code auto_increment}).
   */
  @Generated(INSERT)
  @Column(unique = true, nullable = false, insertable = false, updatable = false)
  private Long uid;

  private String firstName;
  private String lastName;
  private String machine;

  @ManyToMany(mappedBy = "members", fetch = FetchType.LAZY)
  private Set<Group> groups;

  public User(final String firstName, final String lastName)
  {
    this.firstName = firstName;
    this.lastName = lastName;
    this.machine = UUID.randomUUID().toString();
  }
}
