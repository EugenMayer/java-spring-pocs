package de.kontextwork.poc.spring.blaze.fullapp.privilege.model.jpa;

import de.kontextwork.poc.spring.blaze.fullapp.role.model.jpa.Role;
import java.util.Set;
import javax.persistence.*;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Entity
@Accessors(chain = true)
@Table(name = "subject_privilege")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.STRING)
public abstract class Privilege<R extends Role>
{
  public static final String REALM_PRIVILEGE_DISCRIMINATOR = "REALM";
  public static final String GLOBAL_PRIVILEGE_DISCRIMINATOR = "GLOBAL";

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToMany(mappedBy = "privileges", fetch = FetchType.LAZY)
  private Set<R> roles;

  /**
   * The privilege name.
   */
  private String name;

  public Privilege(String name)
  {
    this.name = name;
  }
}
