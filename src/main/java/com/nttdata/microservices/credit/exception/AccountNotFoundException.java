package com.nttdata.microservices.credit.exception;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AccountNotFoundException extends RuntimeException {

    private String message;
    private Integer statusCode;

    public AccountNotFoundException(String message, Integer statusCode) {
        super(message);
        this.message = message;
        this.statusCode = statusCode;
    }

    public AccountNotFoundException(String message) {
        super(message);
    }

    public AccountNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
