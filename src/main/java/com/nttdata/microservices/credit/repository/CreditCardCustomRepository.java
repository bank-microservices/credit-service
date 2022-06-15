package com.nttdata.microservices.credit.repository;

import com.nttdata.microservices.credit.service.dto.CreditCardDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


public interface CreditCardCustomRepository {

  Mono<CreditCardDto> findByAccountNumber(String creditId);

  Flux<CreditCardDto> findByClientDocumentNumber(String documentNumber);
}
