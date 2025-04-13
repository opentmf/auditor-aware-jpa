package org.opentmf.commons.jpa.config;

import java.time.OffsetDateTime;
import java.time.temporal.TemporalAccessor;
import java.util.Optional;
import lombok.experimental.UtilityClass;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.lang.NonNull;

/**
 * @author Gokhan Demir
 */
@UtilityClass
public class AuditorAwareJpaUtil {

  public static final String NA = "n/a";

  public static class OffsetDateTimeProvider implements DateTimeProvider {

    @Override
    @NonNull
    public Optional<TemporalAccessor> getNow() {
      return Optional.of(OffsetDateTime.now());
    }
  }
}
