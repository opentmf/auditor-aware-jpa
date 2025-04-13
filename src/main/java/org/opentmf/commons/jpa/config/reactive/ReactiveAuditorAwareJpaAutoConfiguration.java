package org.opentmf.commons.jpa.config.reactive;

import static org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication.Type.REACTIVE;

import org.opentmf.commons.jpa.config.AuditorAwareJpaUtil.OffsetDateTimeProvider;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.boot.web.reactive.filter.OrderedWebFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.lang.NonNull;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

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
  public OrderedWebFilter securityContextCaptureFilter() {

    return new OrderedWebFilter() {

      @Override
      public int getOrder() {
        return SecurityWebFiltersOrder.AUTHORIZATION.getOrder() + 1;
      }

      @Override
      @NonNull
      public Mono<Void> filter(@NonNull ServerWebExchange exchange, @NonNull WebFilterChain chain) {
        return ReactiveSecurityContextHolder.getContext()
            .doOnNext(SecurityContextCaptureFilter::setThreadLocalSecurityContext)
            .then(chain.filter(exchange))
            .doFinally(signalType -> SecurityContextCaptureFilter.clearThreadLocalSecurityContext());
      }
    };
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