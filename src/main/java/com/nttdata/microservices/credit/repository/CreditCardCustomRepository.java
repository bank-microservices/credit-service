package com.nttdata.microservices.credit.repository;

import com.nttdata.microservices.credit.entity.CreditCard;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


public interface CreditCardCustomRepository {

  Mono<CreditCard> findByAccountNumber(String creditId);

  Flux<CreditCard> findByClientDocumentNumber(String documentNumber);

  Mono<CreditCard> findByClientDocumentAndCardNumber(String documentNumber, String cardNumber);

}
