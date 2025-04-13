package org.opentmf.commons.jpa.exception;

/**
 * @author Gokhan Demir
 */
public class CarNotFoundException extends RuntimeException {

  public CarNotFoundException(String message) {
    super(message);
  }
}
