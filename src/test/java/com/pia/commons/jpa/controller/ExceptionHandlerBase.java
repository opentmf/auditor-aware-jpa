package com.pia.commons.jpa.controller;

import com.pia.commons.jpa.model.ErrorContext;

/**
 * @author Gokhan Demir
 */
public abstract class ExceptionHandlerBase {

  ErrorContext errorContext(String message, int status) {
    var errorContext = new ErrorContext();
    errorContext.setMessage(message);
    errorContext.setStatus(String.valueOf(status));
    return errorContext;
  }
}
