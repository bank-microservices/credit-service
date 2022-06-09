package com.nttdata.microservices.credit.service;

import com.nttdata.microservices.credit.service.dto.CreditDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CreditService {

    Flux<CreditDto> findAllCredits();

    Mono<CreditDto> findByCreditId(String creditId);

    Flux<CreditDto> findByClientId(String clientId);


    Mono<CreditDto> findByAccountNumber(String numberAccount);

    Flux<CreditDto> findByClientDocumentNumber(String customerId);

    Mono<CreditDto> createCredit(CreditDto creditDto);

    Mono<CreditDto> updateCreditAmount(String id, Double creditAmount);

    Mono<CreditDto> updateCreditLimit(String id, Double creditLimit);

}
