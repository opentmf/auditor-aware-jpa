package org.opentmf.commons.jpa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;

/**
 * @author Abdullah Beker
 */
@Getter
@Setter
@MappedSuperclass
public class AuditUpdatable extends Updatable {

  /** record created by this user */
  @CreatedBy
  @Column(length = 100, nullable = false)
  private String createdBy;

  /** record last updated by this user */
  @LastModifiedBy
  @Column(length = 100)
  private String modifiedBy;
}
