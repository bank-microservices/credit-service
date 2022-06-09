package com.nttdata.microservices.credit.proxy.impl;

import com.nttdata.microservices.credit.exception.ClientNotFoundException;
import com.nttdata.microservices.credit.proxy.ClientProxy;
import com.nttdata.microservices.credit.service.dto.ClientDto;
import com.nttdata.microservices.credit.util.RestUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class ClientProxyImpl implements ClientProxy {

    private static final String STATUS_CODE = "Status code : {}";

    private final WebClient webClient;

    public ClientProxyImpl(@Value("${service.client.uri}") String url) {
        this.webClient = WebClient.builder()
                .clientConnector(RestUtils.getDefaultClientConnector())
                .baseUrl(url).build();
    }

    @Override
    public Mono<ClientDto> getClientByDocumentNumber(final String documentNumber) {
        String errorMessage = String.format("There is Client not available with Document Number: %s ", documentNumber);
        return this.webClient.get()
                .uri("/documentNumber/{number}", documentNumber)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, clientResponse -> this.applyError4xx(clientResponse, errorMessage))
                .onStatus(HttpStatus::is5xxServerError, this::applyError5xx)
                .bodyToMono(ClientDto.class);
    }

    @Override
    public Mono<ClientDto> getClientById(final String id) {
        String errorMessage = String.format("Client not found with id: %s ", id);
        return this.webClient.get()
                .uri("/{id}", id)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, clientResponse -> this.applyError4xx(clientResponse, errorMessage))
                .onStatus(HttpStatus::is5xxServerError, this::applyError5xx)
                .bodyToMono(ClientDto.class);
    }


    private Mono<? extends Throwable> applyError4xx(ClientResponse creditResponse, String errorMessage) {
        log.info(STATUS_CODE, creditResponse.statusCode().value());
        if (creditResponse.statusCode().equals(HttpStatus.NOT_FOUND)) {
            return Mono.error(new ClientNotFoundException(errorMessage, creditResponse.statusCode().value()));
        }
        return creditResponse.bodyToMono(String.class)
                .flatMap(response -> Mono.error(new ClientNotFoundException(response, creditResponse.statusCode().value())));
    }

    private Mono<? extends Throwable> applyError5xx(ClientResponse clientResponse) {
        log.info(STATUS_CODE, clientResponse.statusCode().value());
        return clientResponse.bodyToMono(String.class)
                .flatMap(response -> Mono.error(new ClientNotFoundException(response)));
    }
}
