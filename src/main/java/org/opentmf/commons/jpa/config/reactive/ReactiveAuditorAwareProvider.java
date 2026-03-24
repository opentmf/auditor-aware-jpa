package org.opentmf.commons.jpa.config.reactive;

import static org.opentmf.commons.jpa.config.AuditorAwareJpaUtil.NA;

import java.util.Optional;
import org.springframework.data.domain.AuditorAware;
import org.jspecify.annotations.NonNull;
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
    if (securityContext == null) {
      return NA;
    }
    Authentication authentication = securityContext.getAuthentication();
    if (authentication != null && authentication.isAuthenticated()) {
      String name = authentication.getName();
      return name != null ? name : NA;
    }
    return NA;
  }
}
