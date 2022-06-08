package com.nttdata.microservices.credit.proxy.impl;

import com.nttdata.microservices.credit.exception.ClientNotFoundException;
import com.nttdata.microservices.credit.proxy.AccountProxy;
import com.nttdata.microservices.credit.service.dto.AccountDto;
import com.nttdata.microservices.credit.util.RestUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

@Slf4j
@Service
public class AccountProxyImpl implements AccountProxy {

    private static final String STATUS_CODE = "Status code : {}";

    private final WebClient webClient;

    public AccountProxyImpl(@Value("${service.account.uri}") String url) {
        this.webClient = WebClient.builder()
                .clientConnector(RestUtils.getDefaultClientConnector())
                .baseUrl(url).build();
    }

    @Override
    public Mono<AccountDto> update(AccountDto accountDto) {
        return null;
    }

    @Override
    public Flux<AccountDto> findByAccountNumberAndClientDocument(String accountNumber, String documentNumber) {
        Map<String, String> params = Map.of("accountNumber", accountNumber, "documentNumber", documentNumber);
        return this.webClient.get()
                .uri("/number/{accountNumber}/client/{documentNumber}", params)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, (accountResponse -> {
                    log.info(STATUS_CODE, accountResponse.statusCode().value());
                    if (accountResponse.statusCode().equals(HttpStatus.NOT_FOUND)) {
                        return Mono.error(new ClientNotFoundException(
                                String.format("There is Account not available with accountNumber: %s and documentNumber: %s", params.values().toArray()),
                                accountResponse.statusCode().value()));
                    }
                    return accountResponse.bodyToMono(String.class)
                            .flatMap(response -> Mono
                                    .error(new ClientNotFoundException(response, accountResponse.statusCode().value())));
                }))
                .onStatus(HttpStatus::is5xxServerError, (clientResponse -> {
                    log.info(STATUS_CODE, clientResponse.statusCode().value());
                    return clientResponse.bodyToMono(String.class)
                            .flatMap(response -> Mono.error(new ClientNotFoundException(response)));
                }))
                .bodyToFlux(AccountDto.class);
    }

    @Override
    public Mono<AccountDto> findByAccountNumber(String accountNumber) {
        return this.webClient.get()
                .uri("/number/{accountNumber}", accountNumber)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, (accountResponse -> {
                    log.info(STATUS_CODE, accountResponse.statusCode().value());
                    if (accountResponse.statusCode().equals(HttpStatus.NOT_FOUND)) {
                        return Mono.error(new ClientNotFoundException(
                                String.format("There is Account not available with accountNumber: %s ", accountNumber),
                                accountResponse.statusCode().value()));
                    }
                    return accountResponse.bodyToMono(String.class)
                            .flatMap(response -> Mono
                                    .error(new ClientNotFoundException(response, accountResponse.statusCode().value())));
                }))
                .onStatus(HttpStatus::is5xxServerError, (clientResponse -> {
                    log.info(STATUS_CODE, clientResponse.statusCode().value());
                    return clientResponse.bodyToMono(String.class)
                            .flatMap(response -> Mono.error(new ClientNotFoundException(response)));
                }))
                .bodyToMono(AccountDto.class);
    }
}
