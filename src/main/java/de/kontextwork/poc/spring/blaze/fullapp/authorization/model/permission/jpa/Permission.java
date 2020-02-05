package de.kontextwork.poc.spring.blaze.fullapp.authorization.model.permission.jpa;

import de.kontextwork.poc.spring.blaze.fullapp.authorization.model.Scope;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import lombok.*;

@Data
@Entity
@NoArgsConstructor
@DiscriminatorValue("PERMISSION")
@EqualsAndHashCode(callSuper = true)
public final class Permission extends BasePermission
{
  @ManyToMany(mappedBy = "machineName", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
  public Set<CompositePermission> composites = new HashSet<>();

  public Permission(String name, Scope scope, boolean isPublic)
  {
    super(name, scope, isPublic);
  }

  public Permission(String name, Scope scope, boolean isVisible, Set<CompositePermission> composites)
  {
    super(name, scope, isVisible);
    this.setComposites(composites);
  }
}
