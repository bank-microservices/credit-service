package com.nttdata.microservices.credit.exceptionhandler;

import com.nttdata.microservices.credit.exception.BadRequestException;
import com.nttdata.microservices.credit.exception.ClientNotFoundException;
import com.nttdata.microservices.credit.exception.CreditCardNotFoundException;
import com.nttdata.microservices.credit.exception.CreditNotFoundException;
import com.nttdata.microservices.credit.exception.DataValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.support.WebExchangeBindException;

import java.util.stream.Collectors;

@Slf4j
@ControllerAdvice
public class GlobalErrorHandler {

  @ExceptionHandler(WebExchangeBindException.class)
  public ResponseEntity<String> handleRequestBodyError(WebExchangeBindException ex) {
    log.error("Exception caught in handleRequestBodyError :  {} ", ex.getMessage(), ex);
    var error = ex.getBindingResult().getAllErrors().stream()
            .map(DefaultMessageSourceResolvable::getDefaultMessage)
            .sorted()
            .collect(Collectors.joining(","));
    log.error("errorList : {}", error);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
  }

  @ExceptionHandler(BadRequestException.class)
  public ResponseEntity<String> handleClientException(BadRequestException ex) {
    log.error("Exception caught in handleClientException :  {} ", ex.getMessage(), ex);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
  }

  @ExceptionHandler(ClientNotFoundException.class)
  public ResponseEntity<String> handleClientException(ClientNotFoundException ex) {
    log.error("Exception caught in handleClientException :  {} ", ex.getMessage(), ex);
    log.info("Status value is : {}", ex.getStatusCode());
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
  }

  @ExceptionHandler(CreditCardNotFoundException.class)
  public ResponseEntity<String> handleClientException(CreditCardNotFoundException ex) {
    log.error("Exception caught in handleCreditCardException :  {} ", ex.getMessage(), ex);
    log.info("Status value is : {}", ex.getStatusCode());
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
  }

  @ExceptionHandler(CreditNotFoundException.class)
  public ResponseEntity<String> handleClientException(CreditNotFoundException ex) {
    log.error("Exception caught in handleCreditException :  {} ", ex.getMessage(), ex);
    log.info("Status value is : {}", ex.getStatusCode());
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
  }

  @ExceptionHandler(DataValidationException.class)
  public ResponseEntity<String> handleMovieInfoNotfoundException(DataValidationException ex) {
    log.error("Exception caught in handleMovieInfoNotfoundException :  {} ", ex.getMessage(), ex);
    return ResponseEntity.status(HttpStatus.valueOf(ex.getStatusCode())).body(ex.getMessage());
  }

}