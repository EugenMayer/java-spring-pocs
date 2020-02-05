package de.kontextwork.poc.spring.blaze.fullapp.authorization.model.permission.jpa;

import de.kontextwork.poc.spring.blaze.fullapp.authorization.model.Scope;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import lombok.*;

@Data
@Entity
@NoArgsConstructor
@DiscriminatorValue("COMPOSITE_PERMISSION")
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
public final class CompositePermission extends BasePermission
{
  @JoinTable(
    name = "perm_composite_permissions",
    joinColumns = {@JoinColumn(name = "composite_permission_id")},
    inverseJoinColumns = {@JoinColumn(name = "permission_id")}
  )
  @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  private Set<Permission> permissions = new HashSet<>();

  /// Constructor ///

  public CompositePermission(String name, Scope scope, boolean isVisible)
  {
    super(name, scope, isVisible);
  }

  public CompositePermission(String name, Scope scope, boolean isVisible, Set<Permission> permissions)
  {
    super(name, scope, isVisible);
    this.setPermissions(permissions);
  }
}
