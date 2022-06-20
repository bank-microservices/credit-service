package com.nttdata.microservices.credit.exceptionhandler;

import com.nttdata.microservices.credit.exception.BadRequestException;
import com.nttdata.microservices.credit.exception.ClientNotFoundException;
import com.nttdata.microservices.credit.exception.CreditCardNotFoundException;
import com.nttdata.microservices.credit.exception.CreditNotFoundException;
import com.nttdata.microservices.credit.exception.DataValidationException;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.support.WebExchangeBindException;

@Slf4j
@ControllerAdvice
public class GlobalErrorHandler {

  private static final String STATUS_VALUE_IS = "Status value is : {}";

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
  public ResponseEntity<String> handleBadRequestException(BadRequestException ex) {
    log.error("Exception caught in handleBadRequestException :  {} ", ex.getMessage(), ex);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
  }

  @ExceptionHandler(ClientNotFoundException.class)
  public ResponseEntity<String> handleClientNotFoundException(ClientNotFoundException ex) {
    log.error("Exception caught in handleClientNotFoundException :  {} ", ex.getMessage(), ex);
    log.info(STATUS_VALUE_IS, ex.getStatusCode());
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
  }

  @ExceptionHandler(CreditCardNotFoundException.class)
  public ResponseEntity<String> handleCreditCardNotFoundException(CreditCardNotFoundException ex) {
    log.error("Exception caught in handleCreditCardNotFoundException :  {} ", ex.getMessage(), ex);
    log.info(STATUS_VALUE_IS, ex.getStatusCode());
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
  }

  @ExceptionHandler(CreditNotFoundException.class)
  public ResponseEntity<String> handleCreditNotFoundException(CreditNotFoundException ex) {
    log.error("Exception caught in handleCreditNotFoundException :  {} ", ex.getMessage(), ex);
    log.info(STATUS_VALUE_IS, ex.getStatusCode());
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
  }

  @ExceptionHandler(DataValidationException.class)
  public ResponseEntity<String> handleDataValidationException(DataValidationException ex) {
    log.error("Exception caught in handleDataValidationException :  {} ", ex.getMessage(), ex);
    return ResponseEntity.status(HttpStatus.valueOf(ex.getStatusCode())).body(ex.getMessage());
  }

}