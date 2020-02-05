package de.kontextwork.poc.spring.blaze.fullapp.authorization.model.role.jpa;

import de.kontextwork.poc.spring.blaze.fullapp.authorization.model.Scope;
import de.kontextwork.poc.spring.blaze.fullapp.authorization.model.permission.jpa.BasePermission;
import de.kontextwork.poc.spring.blaze.fullapp.authorization.model.permission.jpa.CompositePermission;
import java.util.*;
import java.util.stream.Collectors;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.annotations.NaturalId;
import org.modelmapper.internal.util.Assert;

@Data
@Entity
@NoArgsConstructor
@Accessors(chain = true)
@Table(name = "perm_role")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class AuthRole
{
  @Id
  @EqualsAndHashCode.Include
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(unique = true)
  private String name;

  @NaturalId
  @EqualsAndHashCode.Include
  @Column(columnDefinition = "VARCHAR(200)", unique = true, nullable = false)
  private String machineName;

  @NotNull
  @Enumerated(EnumType.STRING)
  private Scope scope;

  private boolean isVisible;

  @JoinTable(
    name = "perm_role_composite_permissions",
    joinColumns = @JoinColumn(name = "fk_role", referencedColumnName = "id"),
    inverseJoinColumns = @JoinColumn(name = "fk_composite_permission", referencedColumnName = "perm",
      columnDefinition = "VARCHAR(200)")
  )
  @ManyToMany(fetch = FetchType.LAZY)
  public Set<BasePermission> basePermissions = new HashSet<>();

  @PreUpdate
  @PrePersist
  public void assertSanity()
  {
    Assert.state(this.basePermissions.stream().map(BasePermission::getScope).allMatch(s -> s == this.getScope()),
      "COMPOSITE PERMISSIONS scope must match ROLE scope");
  }

  public AuthRole(String name, Scope scope, boolean isVisible, Set<CompositePermission> compositePermissions)
  {
    this.setName(name);
    this.setMachineName(name.toLowerCase().replaceAll("\\s+", "_"));
    this.setScope(scope);
    this.setVisible(isVisible);
    this.setBasePermissions(compositePermissions.stream()
      .map(compositePermission -> (BasePermission) compositePermission)
      .collect(Collectors.toSet()));
  }
}
