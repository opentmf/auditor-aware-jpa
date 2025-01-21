package com.pia.commons.jpa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Version;
import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * @author Gokhan Demir
 */
@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class Insertable {

  /** record created at this date time. */
  @CreatedDate
  @Column(nullable = false)
  private OffsetDateTime createdOn;

  /** record update count for internal optimistic concurrency control */
  @Version
  @Column(nullable = false)
  private int updateCount;
}
