package de.kontextwork.poc.spring.configuration;

import com.blazebit.persistence.Criteria;
import com.blazebit.persistence.CriteriaBuilderFactory;
import com.blazebit.persistence.integration.view.spring.EnableEntityViews;
import com.blazebit.persistence.spi.CriteriaBuilderConfiguration;
import com.blazebit.persistence.spring.data.impl.repository.BlazePersistenceRepositoryFactoryBean;
import com.blazebit.persistence.view.EntityViewManager;
import com.blazebit.persistence.view.spi.EntityViewConfiguration;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.*;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

// @see https://persistence.blazebit.com/documentation/1.3/entity-view/manual/en_US/#anchor-environment-spring
@Configuration
@EnableEntityViews("de.kontextwork.poc.spring")
@EnableJpaRepositories(
  basePackages = "de.kontextwork.poc.spring",
  // @see https://persistence.blazebit.com/documentation/1.4/entity-view/manual/en_US/#spring-data-setup
  repositoryFactoryBeanClass = BlazePersistenceRepositoryFactoryBean.class
)
@ConditionalOnProperty(
  value = "blazepersistance.enabled",
  havingValue = "true",
  matchIfMissing = false)
public class BlazePersistenceConfiguration
{
  @PersistenceUnit
  private EntityManagerFactory entityManagerFactory;

  @Bean
  @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
  @Lazy(false)
  public CriteriaBuilderFactory createCriteriaBuilderFactory()
  {
    CriteriaBuilderConfiguration config = Criteria.getDefault();
    // do some configuration
    return config.createCriteriaBuilderFactory(entityManagerFactory);
  }

  // @see https://persistence.blazebit.com/documentation/1.3/entity-view/manual/en_US/#anchor-environment-spring
  @Bean
  @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
  @Lazy(false)
  // inject the criteria builder factory which will be used along with the entity view manager
  public EntityViewManager createEntityViewManager(
    CriteriaBuilderFactory cbf,
    EntityViewConfiguration entityViewConfiguration
  )
  {
    return entityViewConfiguration.createEntityViewManager(cbf);
  }
}