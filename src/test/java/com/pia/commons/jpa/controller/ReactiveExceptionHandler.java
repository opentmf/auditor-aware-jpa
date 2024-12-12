package com.pia.commons.jpa.controller;

import com.pia.commons.jpa.exception.CarExistsException;
import com.pia.commons.jpa.exception.CarNotFoundException;
import com.pia.commons.jpa.model.ErrorContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication.Type;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reactor.core.publisher.Mono;

/**
 * @author Gokhan Demir
 */
@RestControllerAdvice
@ConditionalOnWebApplication(type = Type.REACTIVE)
@Slf4j
class ReactiveExceptionHandler extends ExceptionHandlerBase {

  @ExceptionHandler(CarExistsException.class)
  public Mono<ResponseEntity<ErrorContext>> handle(CarExistsException exception) {
    var message = exception.getLocalizedMessage();
    return Mono.just(
        ResponseEntity.badRequest().body(errorContext(message, HttpStatus.BAD_REQUEST.value())));
  }

  @ExceptionHandler(CarNotFoundException.class)
  public Mono<ResponseEntity<ErrorContext>> handle(CarNotFoundException exception) {
    var message = exception.getLocalizedMessage();
    return Mono.just(
        ResponseEntity.badRequest().body(errorContext(message, HttpStatus.NOT_FOUND.value())));
  }

  @ExceptionHandler(Exception.class)
  public Mono<ResponseEntity<ErrorContext>> handle(Exception exception) {
    log.error("", exception);
    var message = exception.getLocalizedMessage();
    return Mono.just(
        ResponseEntity.internalServerError()
            .body(errorContext(message, HttpStatus.INTERNAL_SERVER_ERROR.value())));
  }
}
