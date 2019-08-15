package de.kontextwork.poc.spring.many2many.naturalassociation.space;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpaceRepository extends JpaRepository<Space, String>
{
}
