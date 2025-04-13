package org.opentmf.commons.jpa.controller;

import org.opentmf.commons.jpa.exception.CarExistsException;
import org.opentmf.commons.jpa.exception.CarNotFoundException;
import org.opentmf.commons.jpa.model.ErrorContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication.Type;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author Gokhan Demir
 */
@RestControllerAdvice
@ConditionalOnWebApplication(type = Type.SERVLET)
@Slf4j
class ServletExceptionHandler extends ExceptionHandlerBase {

  @ExceptionHandler(CarExistsException.class)
  public ResponseEntity<ErrorContext> handle(CarExistsException exception) {
    var message = exception.getLocalizedMessage();
    return ResponseEntity.badRequest().body(errorContext(message, HttpStatus.BAD_REQUEST.value()));
  }

  @ExceptionHandler(CarNotFoundException.class)
  public ResponseEntity<ErrorContext> handle(CarNotFoundException exception) {
    var message = exception.getLocalizedMessage();
    return ResponseEntity.badRequest().body(errorContext(message, HttpStatus.NOT_FOUND.value()));
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorContext> handle(Exception exception) {
    log.error("", exception);
    var message = exception.getLocalizedMessage();
    return ResponseEntity.internalServerError()
        .body(errorContext(message, HttpStatus.INTERNAL_SERVER_ERROR.value()));
  }
}
