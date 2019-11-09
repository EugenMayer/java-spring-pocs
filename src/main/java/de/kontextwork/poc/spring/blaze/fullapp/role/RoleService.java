package de.kontextwork.poc.spring.blaze.fullapp.role;

import com.blazebit.persistence.view.EntityViewSetting;
import de.kontextwork.poc.spring.blaze.core.RegularEntityViewRepository;
import de.kontextwork.poc.spring.blaze.fullapp.role.model.jpa.Role;
import de.kontextwork.poc.spring.blaze.fullapp.role.model.view.RoleIdView;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleService
{
  private final RoleRepository roleRepository;
  private final RegularEntityViewRepository<Role, Long> regularEntityViewRepository;


  /**
   * Creates new {@link Role}.
   */
  public <R extends Role> R create(final R role)
  {
    return roleRepository.save(role);
  }


  public Optional<RoleIdView> getOneAsIdView(Long id) {
    return regularEntityViewRepository.getOne(EntityViewSetting.create(RoleIdView.class), id);
  }
}
