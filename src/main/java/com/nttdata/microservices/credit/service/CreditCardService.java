package com.nttdata.microservices.credit.service;

import com.nttdata.microservices.credit.service.dto.CreditCardDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CreditCardService {

  Mono<CreditCardDto> findById(String id);

  Flux<CreditCardDto> findAll();

  Mono<CreditCardDto> findByAccountNumber(String accountNumber);

  Flux<CreditCardDto> findByClientDocumentNumber(String documentNumber);

  Mono<CreditCardDto> findByCardNumber(String cardNumber);

  Mono<CreditCardDto> create(CreditCardDto cardDto);

  Mono<CreditCardDto> update(String id, CreditCardDto cardDto);

  Mono<CreditCardDto> partialUpdate(String id, CreditCardDto cardDto);

  Mono<Void> delete(String id);

}
