package de.kontextwork.poc.spring.blaze.fullapp.realm;

import com.blazebit.persistence.view.EntityViewSetting;
import de.kontextwork.poc.spring.blaze.core.RegularEntityViewRepository;
import de.kontextwork.poc.spring.blaze.fullapp.realm.model.jpa.Realm;
import de.kontextwork.poc.spring.blaze.fullapp.realm.model.view.RealmIdView;
import de.kontextwork.poc.spring.blaze.fullapp.role.model.jpa.Role;
import de.kontextwork.poc.spring.blaze.fullapp.role.model.view.RoleIdView;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RealmService
{
  private final RealmRepository realmRepository;
  private final RegularEntityViewRepository<Realm, Long> regularEntityViewRepository;

  /**
   * Creates new {@link Realm}.
   */
  public Realm create(final Realm realm)
  {
    return realmRepository.save(realm);
  }


  public Optional<RealmIdView> getOneAsIdView(Long id) {
    return regularEntityViewRepository.getOne(EntityViewSetting.create(RealmIdView.class), id);
  }
}
