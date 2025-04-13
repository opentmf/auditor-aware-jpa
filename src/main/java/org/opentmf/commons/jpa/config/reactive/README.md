# Foreword

The reactive web application and blocking JPA is not compatible to use in the same place. It is recommended to use `r2dbc` with reactive web applications.

This combination of Reactive Web Application + blocking JPA to enable the AuditorAware interface is provided with these considerations:

We need to make sure the SecurityContext is propagated and accessible when getCurrentAuditor() is invoked. A reliable way to do this is to eagerly capture the SecurityContext in the reactive pipeline and store it in a thread-local variable that is accessible in `AuditorAware`.

## Implementation

### 1. Custom Context Capture Filter

Use a WebFilter to capture the SecurityContext and store it in a ThreadLocal for later retrieval.

```java
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

public class SecurityContextCaptureFilter implements WebFilter {

    private static final ThreadLocal<SecurityContext> contextHolder = new ThreadLocal<>();

    public static SecurityContext getThreadLocalSecurityContext() {
        return contextHolder.get();
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        return ReactiveSecurityContextHolder.getContext()
            .doOnNext(securityContext -> contextHolder.set(securityContext))
            .then(chain.filter(exchange))
            .doFinally(signalType -> contextHolder.remove());
    }
}
```
- The filter captures the `SecurityContext` at the start of a request and removes it when the request completes.
- It ensures the SecurityContext is available for blocking operations like `AuditorAware`.

### 2. Register the Filter
```java
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.server.WebFilter;

@Configuration
public class WebFilterConfig {

    @Bean
    public WebFilter securityContextCaptureFilter() {
        return new SecurityContextCaptureFilter();
    }
}
```

### 3. Update AuditorAware Implementation
Modify the `AuditorAware` implementation to use the thread-local context.

```java
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.context.SecurityContext;

import java.util.Optional;

public class BlockingAuditorAware implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        SecurityContext securityContext = SecurityContextCaptureFilter.getThreadLocalSecurityContext();
        if (securityContext != null && securityContext.getAuthentication() != null) {
            Object principal = securityContext.getAuthentication().getPrincipal();
            return Optional.of(principal instanceof String
                ? (String) principal
                : ((org.springframework.security.core.userdetails.UserDetails) principal).getUsername());
        }
        return Optional.empty();
    }
}
```

### 4. Register AuditorAware Bean
Add the BlockingAuditorAware to your Spring configuration.

```java
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class JpaAuditingConfig {

    @Bean
    public AuditorAware<String> auditorProvider() {
        return new BlockingAuditorAware();
    }
}
```

## How It Works
- The `SecurityContextCaptureFilter` ensures that the `SecurityContext` is stored in a `ThreadLocal` variable for the duration of the request.
- The `BlockingAuditorAware` retrieves the `SecurityContext` from the `ThreadLocal` and uses it to fetch the current auditor's username.

## Important Notes

### 1. ThreadLocal Management:
- Ensure the ThreadLocal is cleared (remove()) after each request to avoid memory leaks.

### 2. Blocking in Reactive Context:
- This approach introduces blocking for `AuditorAware` compatibility but isolates it to the JPA auditing process.
- For fully reactive applications, consider using Spring Data R2DBC and `ReactiveAuditorAware`.

### 3. Testing
- Mock the `ReactiveSecurityContextHolder` during tests or prepopulate the ThreadLocal to simulate the captured context.

## Summary
Summary
This solution ensures the `SecurityContext` is available to `AuditorAware` in a synchronous, blocking manner while respecting the reactive context during request processing. It's a practical workaround for integrating JPA auditing with reactive applications.
