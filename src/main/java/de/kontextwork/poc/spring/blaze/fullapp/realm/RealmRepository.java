package de.kontextwork.poc.spring.blaze.fullapp.realm;

import de.kontextwork.poc.spring.blaze.fullapp.realm.model.jpa.Realm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RealmRepository extends JpaRepository<Realm, Long>
{
}
