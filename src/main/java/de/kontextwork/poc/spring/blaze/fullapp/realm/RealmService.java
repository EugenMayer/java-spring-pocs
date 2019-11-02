package de.kontextwork.poc.spring.blaze.fullapp.realm;

import de.kontextwork.poc.spring.blaze.fullapp.realm.model.jpa.Realm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RealmService
{
  private final RealmRepository realmRepository;

  /**
   * Creates new {@link Realm}.
   */
  public Realm create(final Realm realm)
  {
    return realmRepository.save(realm);
  }

}
