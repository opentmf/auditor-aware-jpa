package org.opentmf.commons.jpa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;

/**
 * @author Gokhan Demir
 */
@Getter
@Setter
@MappedSuperclass
public class AuditInsertable extends Insertable {

  /** record created by this user */
  @CreatedBy
  @Column(length = 100, nullable = false, updatable = false)
  private String createdBy;
}
