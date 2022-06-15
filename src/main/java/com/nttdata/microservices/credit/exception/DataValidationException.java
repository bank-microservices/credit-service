package com.nttdata.microservices.credit.exception;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class DataValidationException extends RuntimeException {

  private String message;
  private Integer statusCode;

  public DataValidationException(String message) {
    super(message);
    this.message = message;
  }

  public DataValidationException(String message, Integer statusCode) {
    super(message);
    this.message = message;
    this.statusCode = statusCode;
  }

  public DataValidationException(String message, Throwable cause) {
    super(message, cause);
    this.message = message;
  }
}
