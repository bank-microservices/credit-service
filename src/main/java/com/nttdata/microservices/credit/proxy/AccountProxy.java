package com.nttdata.microservices.credit.proxy;

import com.nttdata.microservices.credit.service.dto.AccountDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface AccountProxy {

  Flux<AccountDto> findByAccountNumberAndClientDocument(String accountNumber, String documentNumber);

  Mono<AccountDto> findByAccountNumber(String accountNumber);
}
