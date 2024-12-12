package com.pia.commons.jpa.config.servlet;

import static org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication.Type.SERVLET;

import com.pia.commons.jpa.config.AuditorAwareJpaUtil.OffsetDateTimeProvider;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * @author Gokhan Demir
 */
@AutoConfiguration(after = JpaRepositoriesAutoConfiguration.class)
@ConditionalOnWebApplication(type = SERVLET)
@EnableJpaAuditing(
    modifyOnCreate = false,
    dateTimeProviderRef = "servletOffsetDateTimeProvider",
    auditorAwareRef = "servletAuditorAware")
public class ServletAuditorAwareJpaAutoConfiguration {

  @Bean
  public DateTimeProvider servletOffsetDateTimeProvider() {
    return new OffsetDateTimeProvider();
  }

  @Bean
  public AuditorAware<String> servletAuditorAware() {
    return new ServletAuditorAwareProvider();
  }
}
