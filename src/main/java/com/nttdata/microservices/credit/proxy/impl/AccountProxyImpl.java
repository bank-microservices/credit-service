package com.nttdata.microservices.credit.proxy.impl;


import com.nttdata.microservices.credit.exception.AccountNotFoundException;
import com.nttdata.microservices.credit.proxy.AccountProxy;
import com.nttdata.microservices.credit.service.dto.AccountDto;
import com.nttdata.microservices.credit.util.RestUtils;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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
  public Flux<AccountDto> findByAccountNumberAndClientDocument(String accountNumber,
                                                               String documentNumber) {
    Map<String, String> params =
        Map.of("accountNumber", accountNumber, "documentNumber", documentNumber);
    String errorMessage = String.format(
        "There is Account not available with accountNumber: %s and documentNumber: %s",
        params.values().toArray());
    return this.webClient.get()
        .uri("/number/{accountNumber}/client/{documentNumber}", params)
        .retrieve()
        .onStatus(HttpStatus::is4xxClientError,
            clientResponse -> this.applyError4xx(clientResponse, errorMessage))
        .onStatus(HttpStatus::is5xxServerError, this::applyError5xx)
        .bodyToFlux(AccountDto.class);
  }

  @Override
  public Mono<AccountDto> findByAccountNumber(String accountNumber) {
    String errorMessage =
        String.format("There is Account not available with accountNumber: %s ", accountNumber);
    return this.webClient.get()
        .uri("/number/{accountNumber}", accountNumber)
        .retrieve()
        .onStatus(HttpStatus::is4xxClientError,
            clientResponse -> this.applyError4xx(clientResponse, errorMessage))
        .onStatus(HttpStatus::is5xxServerError, this::applyError5xx)
        .bodyToMono(AccountDto.class);
  }

  private Mono<? extends Throwable> applyError4xx(ClientResponse creditResponse,
                                                  String errorMessage) {
    log.info(STATUS_CODE, creditResponse.statusCode().value());
    if (creditResponse.statusCode().equals(HttpStatus.NOT_FOUND)) {
      return Mono.error(
          new AccountNotFoundException(errorMessage, creditResponse.statusCode().value()));
    }
    return creditResponse.bodyToMono(String.class)
        .flatMap(response -> Mono.error(
            new AccountNotFoundException(response, creditResponse.statusCode().value())));
  }

  private Mono<? extends Throwable> applyError5xx(ClientResponse clientResponse) {
    log.info(STATUS_CODE, clientResponse.statusCode().value());
    return clientResponse.bodyToMono(String.class)
        .flatMap(response -> Mono.error(new AccountNotFoundException(response)));
  }
}
