package org.opentmf.commons.jpa.model;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Gokhan Demir
 */
@Getter
@Setter
public final class CarDto extends AuditUpdatableDto {

  private String model;
  private String color;
  private int builtYear;
}
