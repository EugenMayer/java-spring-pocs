package de.kontextwork.poc.spring.blaze.subject.model.jpa;

import javax.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Generated;

import static org.hibernate.annotations.GenerationTime.INSERT;

@Data
@Entity
@Accessors(chain = true)
@DiscriminatorValue("USER")
@Table(name = "subject_user")
@EqualsAndHashCode(callSuper = true)
public class User extends Subject
{
  /**
   * This is a legacy {@code UID} that still has some assosiations attached, so we need to
   * maintain this field when creating a new {@link User} (e.g. {@code auto_increment}).
   */
  @Generated(INSERT)
  @Column(unique = true, nullable = false, insertable = false, updatable = false)
  private Long uid;

  private String firstName;
  private String lastName;

  public User(final String firstName, final String lastName)
  {
    this.firstName = firstName;
    this.lastName = lastName;
  }
}
