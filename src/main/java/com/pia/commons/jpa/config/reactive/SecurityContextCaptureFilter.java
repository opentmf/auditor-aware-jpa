package com.pia.commons.jpa.config.reactive;

import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

/**
 * @author Gokhan Demir
 */
public class SecurityContextCaptureFilter implements WebFilter {

  private static final ThreadLocal<SecurityContext> contextHolder = new ThreadLocal<>();

  public static SecurityContext getThreadLocalSecurityContext() {
    return contextHolder.get();
  }

  @Override
  public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
    return ReactiveSecurityContextHolder.getContext()
        .doOnNext(contextHolder::set)
        .then(chain.filter(exchange))
        .doFinally(signalType -> contextHolder.remove());
  }
}
