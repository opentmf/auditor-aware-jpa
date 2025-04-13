package org.opentmf.commons.jpa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.LastModifiedDate;

/**
 * @author Gokhan Demir
 */
@Getter
@Setter
@MappedSuperclass
public class Updatable extends Insertable {

  /** record last updated at this date time. */
  @LastModifiedDate
  @Column
  private OffsetDateTime modifiedOn;
}
