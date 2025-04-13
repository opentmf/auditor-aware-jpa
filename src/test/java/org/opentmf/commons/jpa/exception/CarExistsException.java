package org.opentmf.commons.jpa.exception;

/**
 * @author Gokhan Demir
 */
public class CarExistsException extends RuntimeException {

  public CarExistsException(String message) {
    super(message);
  }
}
