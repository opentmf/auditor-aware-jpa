package com.pia.commons.jpa.model;

import com.pia.commons.jpa.entity.Updatable;
import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Gokhan Demir
 */
@Getter
@Setter
public class AuditUpdatableDto extends Updatable {

  private OffsetDateTime modifiedOn;
  private String modifiedBy;
  private OffsetDateTime createdOn;
  private String createdBy;
  private int updateCount;
}
