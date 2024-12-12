package com.pia.commons.jpa.config.reactive;

import static org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication.Type.REACTIVE;

import com.pia.commons.jpa.config.AuditorAwareJpaUtil.OffsetDateTimeProvider;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.server.WebFilter;

/**
 * @author Gokhan Demir
 */
@AutoConfiguration(after = JpaRepositoriesAutoConfiguration.class)
@ConditionalOnWebApplication(type = REACTIVE)
@EnableJpaAuditing(
    modifyOnCreate = false,
    dateTimeProviderRef = "reactiveOffsetDateTimeProvider",
    auditorAwareRef = "reactiveAuditorAware")
public class ReactiveAuditorAwareJpaAutoConfiguration {

  @Bean
  public WebFilter securityContextCaptureFilter() {
    return new SecurityContextCaptureFilter();
  }

  @Bean
  public DateTimeProvider reactiveOffsetDateTimeProvider() {
    return new OffsetDateTimeProvider();
  }

  @Bean
  public AuditorAware<String> reactiveAuditorAware() {
    return new ReactiveAuditorAwareProvider();
  }

}