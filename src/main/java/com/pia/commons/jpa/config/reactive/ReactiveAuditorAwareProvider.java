package com.pia.commons.jpa.config.reactive;

import static com.pia.commons.jpa.config.AuditorAwareJpaUtil.NA;

import java.util.Optional;
import org.springframework.data.domain.AuditorAware;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;

/**
 * @author Gokhan Demir
 */
public class ReactiveAuditorAwareProvider implements AuditorAware<String> {

  @Override
  @NonNull
  public Optional<String> getCurrentAuditor() {
    SecurityContext securityContext = SecurityContextCaptureFilter.getThreadLocalSecurityContext();
    return Optional.of(getUsername(securityContext));
  }

  private String getUsername(SecurityContext securityContext) {
    Authentication authentication = securityContext.getAuthentication();
    return authentication != null && authentication.isAuthenticated()
        ? authentication.getName()
        : NA;
  }
}
