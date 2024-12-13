package com.pia.commons.jpa.config.reactive;

import lombok.experimental.UtilityClass;
import org.springframework.security.core.context.SecurityContext;

/**
 * @author Gokhan Demir
 */
@UtilityClass
public class SecurityContextCaptureFilter {

  private static final ThreadLocal<SecurityContext> contextHolder = new ThreadLocal<>();

  public static void setThreadLocalSecurityContext(SecurityContext context) {
    contextHolder.set(context);
  }

  public static void clearThreadLocalSecurityContext() {
    contextHolder.remove();
  }

  public static SecurityContext getThreadLocalSecurityContext() {
    return contextHolder.get();
  }
}
