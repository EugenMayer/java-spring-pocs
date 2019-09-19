package de.kontextwork.poc.spring.configuration;

import com.blazebit.persistence.spring.data.impl.repository.BlazePersistenceRepositoryFactoryBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(
  basePackages = "de.kontextwork.poc.spring",
  // @see https://persistence.blazebit.com/documentation/1.4/entity-view/manual/en_US/#spring-data-setup
  repositoryFactoryBeanClass = BlazePersistenceRepositoryFactoryBean.class
)
@ConditionalOnProperty(
  value = "blazepersistance.enabled",
  havingValue = "true",
  matchIfMissing = false)
public class JpaBlazeConfiguration
{
}
