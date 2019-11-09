package de.kontextwork.poc.spring.configuration;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.*;

/**
 * Configuration for all our REST controllers Adds all the interceptors and cors mapping
 */
@Configuration
@RequiredArgsConstructor
// we mainly use this during tests to use a custom WebMVC configuration
@ConditionalOnProperty(
  value = "application.webmvc.disabled",
  havingValue = "false",
  matchIfMissing = true)
public class RestConfiguration implements WebMvcConfigurer
{
  /**
   * mainly ignore null fields globally for now
   */
  @Override
  public void extendMessageConverters(List<HttpMessageConverter<?>> converters)
  {
    for (HttpMessageConverter converter : converters) {
      if (converter instanceof MappingJackson2HttpMessageConverter) {
        ObjectMapper mapper = ((MappingJackson2HttpMessageConverter) converter).getObjectMapper();
        mapper.setSerializationInclusion(Include.NON_NULL);
      }
    }
  }
}