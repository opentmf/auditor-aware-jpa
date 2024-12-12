package com.pia.commons.jpa.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public final class ErrorContext {

  private String status;
  private String message;
}
