package de.kontextwork.poc.spring.configuration;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(
  basePackages = "de.kontextwork.poc.spring"
)
@ConditionalOnProperty(
  value = "blazepersistance.enabled",
  havingValue = "false",
  matchIfMissing = true)
public class HibernateConfiguration
{
}
