package de.kontextwork.poc.spring.blaze.fullapp.authorization.model.permission.jpa;

import de.kontextwork.poc.spring.blaze.fullapp.authorization.model.Scope;
import de.kontextwork.poc.spring.blaze.fullapp.authorization.model.role.jpa.AuthRole;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.annotations.NaturalId;
import org.modelmapper.internal.util.Assert;

@Data
@Entity
@NoArgsConstructor
@Accessors(chain = true)
@Table(name = "perm_permission")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.STRING)
public abstract class BasePermission implements Serializable
{
  @Id
  @EqualsAndHashCode.Include
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(unique = true)
  private String name;

  @Enumerated(EnumType.STRING)
  private Scope scope;

  @NaturalId
  @EqualsAndHashCode.Include
  @Column(name = "perm", unique = true, nullable = true, columnDefinition = "VARCHAR(200)")
  private String machineName;

  private boolean isVisible;

  @JoinTable(
    name = "perm_role_composite_permissions",
    joinColumns = @JoinColumn(
      name = "fk_composite_permission",
      referencedColumnName = "perm",
      columnDefinition = "VARCHAR(200)"
    ),
    inverseJoinColumns = @JoinColumn(
      name = "fk_role",
      referencedColumnName = "id"
    )
  )
  @ManyToMany(fetch = FetchType.LAZY)
  private Set<AuthRole> roles = new HashSet<>();

  @PreUpdate
  @PrePersist
  public void assertSanity()
  {
    if (this.scope == Scope.SYSTEM) {
      Assert.state(!this.isVisible, "SYSTEM permissions must not be PUBLIC");
    }
  }

  @Deprecated
  public BasePermission(String name, Scope scope, boolean isVisible)
  {
    this.setName(name);
    this.setMachineName("__" + name.toLowerCase().replaceAll("\\s+", "_"));
    this.setScope(scope);
    this.setVisible(isVisible);
  }
}
