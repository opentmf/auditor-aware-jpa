package org.opentmf.commons.jpa.config.servlet;

import static org.opentmf.commons.jpa.config.AuditorAwareJpaUtil.NA;

import java.util.Optional;
import org.springframework.data.domain.AuditorAware;
import org.jspecify.annotations.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * @author Gokhan Demir
 */
public class ServletAuditorAwareProvider implements AuditorAware<String> {

  @Override
  @NonNull
  public Optional<String> getCurrentAuditor() {
    return getUsername(SecurityContextHolder.getContext());
  }

  private Optional<String> getUsername(SecurityContext securityContext) {
    Authentication authentication = securityContext.getAuthentication();
    if (authentication != null && authentication.isAuthenticated()) {
      String name = authentication.getName();
      return Optional.of(name != null ? name : NA);
    }
    return Optional.of(NA);
  }

}
