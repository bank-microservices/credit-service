package com.nttdata.microservices.credit.exception;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CreditCardNotFoundException extends RuntimeException {

  private String message;
  private Integer statusCode;

  public CreditCardNotFoundException(String message, Integer statusCode) {
    super(message);
    this.message = message;
    this.statusCode = statusCode;
  }

  public CreditCardNotFoundException(String message) {
    super(message);
  }

  public CreditCardNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }
}
