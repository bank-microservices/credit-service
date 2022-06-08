package com.nttdata.microservices.credit.repository;

import com.nttdata.microservices.credit.service.dto.CreditCardDto;
import reactor.core.publisher.Flux;


public interface CreditCardCustomRepository {

    Flux<CreditCardDto> findByClientDocumentNumber(String documentNumber);
}
