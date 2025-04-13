package org.opentmf.commons.jpa.model;

import org.opentmf.commons.jpa.entity.Updatable;
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
