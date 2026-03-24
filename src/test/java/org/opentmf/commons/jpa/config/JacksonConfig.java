package org.opentmf.commons.jpa.config;

import org.opentmf.commons.util.JacksonUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import tools.jackson.databind.json.JsonMapper;

/**
 * @author Gokhan Demir
 */
@Configuration
public class JacksonConfig {

  @Primary
  @Bean
  public JsonMapper jsonMapper() {
    return JacksonUtil.getDefaultJsonMapper();
  }
}
