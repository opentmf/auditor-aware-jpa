package org.opentmf.commons.jpa.config.reactive;

import static org.opentmf.commons.jpa.config.AuditorAwareJpaUtil.NA;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;

/**
 * Unit tests for ReactiveAuditorAwareProvider covering all branches.
 * 
 * @author Gokhan Demir
 */
class ReactiveAuditorAwareProviderTest {

  private ReactiveAuditorAwareProvider provider;
  private SecurityContext securityContext;

  @BeforeEach
  void setUp() {
    provider = new ReactiveAuditorAwareProvider();
    securityContext = mock(SecurityContext.class);
  }

  @AfterEach
  void tearDown() {
    SecurityContextCaptureFilter.clearThreadLocalSecurityContext();
  }

  @Test
  void getCurrentAuditor_whenSecurityContextIsNull_returnsNA() {
    // Given: securityContext is null (ThreadLocal not set)
    SecurityContextCaptureFilter.clearThreadLocalSecurityContext();

    // When
    Optional<String> result = provider.getCurrentAuditor();

    // Then
    assertTrue(result.isPresent());
    assertEquals(NA, result.get());
  }

  @Test
  void getCurrentAuditor_whenAuthenticationIsNull_returnsNA() {
    // Given: securityContext exists but authentication is null
    when(securityContext.getAuthentication()).thenReturn(null);
    SecurityContextCaptureFilter.setThreadLocalSecurityContext(securityContext);

    // When
    Optional<String> result = provider.getCurrentAuditor();

    // Then
    assertTrue(result.isPresent());
    assertEquals(NA, result.get());
  }

  @Test
  void getCurrentAuditor_whenAuthenticationIsNotAuthenticated_returnsNA() {
    // Given: authentication exists but is not authenticated
    Authentication authentication = mock(Authentication.class);
    when(securityContext.getAuthentication()).thenReturn(authentication);
    when(authentication.isAuthenticated()).thenReturn(false);
    SecurityContextCaptureFilter.setThreadLocalSecurityContext(securityContext);

    // When
    Optional<String> result = provider.getCurrentAuditor();

    // Then
    assertTrue(result.isPresent());
    assertEquals(NA, result.get());
  }

  @Test
  void getCurrentAuditor_whenAuthenticationIsAuthenticatedButNameIsNull_returnsNA() {
    // Given: authentication is authenticated but getName() returns null
    Authentication authentication = mock(Authentication.class);
    when(securityContext.getAuthentication()).thenReturn(authentication);
    when(authentication.isAuthenticated()).thenReturn(true);
    when(authentication.getName()).thenReturn(null);
    SecurityContextCaptureFilter.setThreadLocalSecurityContext(securityContext);

    // When
    Optional<String> result = provider.getCurrentAuditor();

    // Then
    assertTrue(result.isPresent());
    assertEquals(NA, result.get());
  }

  @Test
  void getCurrentAuditor_whenAuthenticationIsAuthenticatedAndNameIsNotNull_returnsName() {
    // Given: authentication is authenticated and getName() returns a valid name
    String expectedUsername = "testuser@example.com";
    Authentication authentication = mock(Authentication.class);
    when(securityContext.getAuthentication()).thenReturn(authentication);
    when(authentication.isAuthenticated()).thenReturn(true);
    when(authentication.getName()).thenReturn(expectedUsername);
    SecurityContextCaptureFilter.setThreadLocalSecurityContext(securityContext);

    // When
    Optional<String> result = provider.getCurrentAuditor();

    // Then
    assertTrue(result.isPresent());
    assertEquals(expectedUsername, result.get());
  }

  @Test
  void getCurrentAuditor_whenAuthenticationIsAuthenticatedAndNameIsEmptyString_returnsName() {
    // Given: authentication is authenticated and getName() returns empty string
    // (edge case: empty string is not null, so it should be returned as-is)
    String expectedUsername = "";
    Authentication authentication = mock(Authentication.class);
    when(securityContext.getAuthentication()).thenReturn(authentication);
    when(authentication.isAuthenticated()).thenReturn(true);
    when(authentication.getName()).thenReturn(expectedUsername);
    SecurityContextCaptureFilter.setThreadLocalSecurityContext(securityContext);

    // When
    Optional<String> result = provider.getCurrentAuditor();

    // Then
    assertTrue(result.isPresent());
    assertEquals(expectedUsername, result.get());
  }
}
