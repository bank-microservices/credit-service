package com.nttdata.microservices.credit.repository;

import com.nttdata.microservices.credit.service.dto.CreditDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


public interface CreditCustomRepository {

  Flux<CreditDto> findAllCredits();

  Mono<CreditDto> findByCreditId(String creditId);

  Flux<CreditDto> findByClientId(String clientId);

  Flux<CreditDto> findByClientDocumentNumber(String documentNumber);

}
