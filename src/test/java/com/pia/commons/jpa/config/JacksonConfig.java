package com.pia.commons.jpa.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pia.commons.util.JacksonUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * @author Gokhan Demir
 */
@Configuration
public class JacksonConfig {

  @Primary
  @Bean
  public ObjectMapper objectMapper() {
    return JacksonUtil.getDefaultObjectMapper();
  }
}
