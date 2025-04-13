package org.opentmf.commons.jpa.model;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Gokhan Demir
 */
@Getter
@Setter
public final class CarCreateDto extends CarUpdateDto {

  private String model;
}
