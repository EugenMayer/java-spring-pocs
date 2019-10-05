package de.kontextwork.poc.spring.blaze;

import de.kontextwork.poc.spring.blaze.model.jpa.BlazeUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlazeUserRepository extends JpaRepository<BlazeUser, Integer>
{
}
