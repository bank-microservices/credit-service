package com.nttdata.microservices.credit.exception;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ClientNotFoundException extends RuntimeException {

  private String message;
  private Integer statusCode;

  public ClientNotFoundException(String message, Integer statusCode) {
    super(message);
    this.message = message;
    this.statusCode = statusCode;
  }

  public ClientNotFoundException(String message) {
    super(message);
  }

  public ClientNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }
}
