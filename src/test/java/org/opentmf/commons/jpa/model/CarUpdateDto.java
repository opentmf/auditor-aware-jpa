package org.opentmf.commons.jpa.model;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Gokhan Demir
 */
@Getter
@Setter
public class CarUpdateDto {

  private String color;
  private int builtYear;
}
