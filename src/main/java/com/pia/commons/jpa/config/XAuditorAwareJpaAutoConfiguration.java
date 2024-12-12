package com.pia.commons.jpa.config;

import static org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication.Type.REACTIVE;

import java.time.OffsetDateTime;
import java.time.temporal.TemporalAccessor;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;

/**
 * @author Gokhan Demir
 */
@AutoConfiguration(after = JpaRepositoriesAutoConfiguration.class)
@ConditionalOnProperty(name = "spring.data.jpa.repositories.enabled", matchIfMissing = true)
@ConditionalOnClass(JpaRepository.class)
@ConditionalOnWebApplication(type = REACTIVE)
@EnableJpaAuditing(
    modifyOnCreate = false,
    dateTimeProviderRef = "offsetDateTimeProvider",
    auditorAwareRef = "auditorAware")
public class XAuditorAwareJpaAutoConfiguration {

  private static final String DEFAULT_AUDITOR = "n/a";
  private static final Logger log = LoggerFactory.getLogger(XAuditorAwareJpaAutoConfiguration.class);

  @Bean
  public DateTimeProvider offsetDateTimeProvider() {
    return new OffsetDateTimeProvider();
  }

  @Bean
  public AuditorAware<String> auditorAware() {
    return new AuditorAwareProvider();
  }

  static class OffsetDateTimeProvider implements DateTimeProvider {
    @Override
    @NonNull
    public Optional<TemporalAccessor> getNow() {
      return Optional.of(OffsetDateTime.now());
    }
  }

  static class AuditorAwareProvider implements AuditorAware<String>  {

    @Override
    @NonNull
    public Optional<String> getCurrentAuditor() {

      return ReactiveSecurityContextHolder.getContext()
          .map(this::getUsername)
          .doOnNext(s -> log.debug("Resolved Username: {}", s))
          .blockOptional();
    }

    private String getUsername(SecurityContext securityContext) {
      Authentication authentication = securityContext.getAuthentication();
      if (authentication == null) {
        log.debug("Authentication is null");
      } else {
        log.debug("isAuthenticated: {}", authentication.isAuthenticated());
        log.debug("SecurityContext class name: {}", securityContext.getClass().getSimpleName());
    }
      return           authentication != null && authentication.isAuthenticated()
              ? authentication.getName()
              : DEFAULT_AUDITOR;
    }
  }
}
