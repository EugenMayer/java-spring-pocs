package de.kontextwork.poc.spring.blaze.subject.model.jpa.role;

import de.kontextwork.poc.spring.blaze.subject.model.jpa.member.GlobalRoleMembership;
import de.kontextwork.poc.spring.blaze.subject.model.jpa.member.RealmRoleMembership;
import de.kontextwork.poc.spring.blaze.subject.model.jpa.privilege.Privilege;
import java.util.Set;
import javax.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;

@Data
@Entity
@Accessors(chain = true)
@Table(name = "subject_role")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.STRING)
public abstract class Role<P extends Privilege>
{
  public static final String REALM_ROLE_DISCRIMINATOR = "REALM";
  public static final String GLOBAL_ROLE_DISCRIMINATOR = "GLOBAL";

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  /**
   * The roles name in uppercase.
   */
  private String name;

  /**
   * Set of {@link Privilege} assigned to this {@link Role}.
   */
  //@Setter(AccessLevel.NONE)
  @ManyToMany(fetch = FetchType.LAZY)
  protected Set<P> privileges;

  /**
   * Owner of this {@link Role}.
   */
  @OneToOne(mappedBy = "role", fetch = FetchType.LAZY)
  private GlobalRoleMembership globalRoleMembership;

  /**
   * Owner of this {@link Role}.
   */
  @OneToOne(mappedBy = "role", fetch = FetchType.LAZY)
  private RealmRoleMembership realmRoleMembership;

  public Role(final String name, Set<P> privileges)
  {
    this.name = name;
    this.privileges = privileges;
  }
}
