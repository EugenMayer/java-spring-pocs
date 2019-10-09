package de.kontextwork.poc.spring.blaze.user;

import de.kontextwork.poc.spring.blaze.user.model.jpa.BlazeUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlazeUserRepository extends JpaRepository<BlazeUser, Integer>
{
}
