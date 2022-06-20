package com.nttdata.microservices.credit.proxy;

import com.nttdata.microservices.credit.service.dto.ClientDto;
import reactor.core.publisher.Mono;

public interface ClientProxy {

  Mono<ClientDto> getClientByDocumentNumber(String documentNumber);

}
