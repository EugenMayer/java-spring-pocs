package de.kontextwork.poc.spring.blaze.subject;

import de.kontextwork.poc.spring.blaze.subject.model.jpa.subject.Realm;
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
