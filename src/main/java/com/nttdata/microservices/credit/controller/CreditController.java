package com.nttdata.microservices.credit.controller;

import com.nttdata.microservices.credit.service.CreditService;
import com.nttdata.microservices.credit.service.dto.BalanceDto;
import com.nttdata.microservices.credit.service.dto.CreditDto;
import com.nttdata.microservices.credit.util.ResponseUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/credit")
public class CreditController {

  private final CreditService creditService;

  /**
   * It returns a Flux of CreditDto objects
   *
   * @return A Flux of CreditDto objects.
   */
  @GetMapping
  public Flux<CreditDto> getAll() {
    log.info("list All Credits");
    return creditService.findAllCredits();
  }

  /**
   * It returns a Mono of a ResponseEntity of a CreditDto
   *
   * @param accountNumber The accountNumber of the credit to be retrieved.
   * @return A Mono<ResponseEntity<CreditDto>>
   */
  @GetMapping("/account-number/{number}")
  public Mono<ResponseEntity<CreditDto>> findByAccountNumber(@PathVariable("number") String accountNumber) {
    log.info("find Credit by accountNumber: {}", accountNumber);
    Mono<CreditDto> dtoMono = creditService.findByAccountNumber(accountNumber);
    return ResponseUtil.wrapOrNotFound(dtoMono);
  }

  /**
   * This returns the credit related to a specific client
   *
   * @param documentNumber The document number of the client.
   * @return A Flux of CreditDto
   */
  @GetMapping("/client-document/{document-number}")
  public Flux<CreditDto> findNumber(@PathVariable("document-number") String documentNumber) {
    log.info("find Credit by documentNumber: {}", documentNumber);
    return creditService.findByClientDocumentNumber(documentNumber);
  }

  /**
   * It returns a Mono of a ResponseEntity of a CreditDto
   *
   * @param clientId The credit to be created.
   * @return A Mono<ResponseEntity<CreditDto>>
   */
  @GetMapping("/client/{clientId}")
  public Flux<CreditDto> findByClientId(@PathVariable("clientId") String clientId) {
    log.info("find Credit by clientId: {}", clientId);
    return creditService.findByClientId(clientId);
  }

  @GetMapping("/balance/{accountNumber}")
  public Mono<ResponseEntity<BalanceDto>> getBalance(@PathVariable String accountNumber) {
    log.info("get Balance Credit by accountNumber: {}", accountNumber);
    return creditService.getBalance(accountNumber)
        .map(ResponseEntity::ok)
        .defaultIfEmpty(ResponseEntity.notFound().build());
  }

  /**
   * It takes a CreditDto object, validates it, and then passes it to the
   * creditService.create()
   * function
   *
   * @param creditDto The object that will be created.
   * @return A Mono of CreditDto
   */
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public Mono<CreditDto> createCredit(@Valid @RequestBody CreditDto creditDto) {
    log.info("create new Credit");
    return creditService.createCredit(creditDto);
  }

  /**
   * It takes a credit id and an amount, and updates the credit amount with the
   * given amount
   *
   * @param creditId The id of the credit to update
   * @param amount   The amount of credit to be added to the account.
   * @return A Mono of CreditDto
   */
  @PutMapping("/amount/{creditId}/{amount}")
  @ResponseStatus(HttpStatus.OK)
  public Mono<CreditDto> updateAmount(@PathVariable String creditId, @PathVariable Double amount) {
    log.info("update Credit ({}) amount: {}", creditId, amount);
    return creditService.updateCreditAmount(creditId, amount);
  }

  /**
   * It takes a credit id and a new limit, and returns a Mono of the updated
   * CreditDto
   *
   * @param creditId The id of the credit to update
   * @param limit    The new credit limit to be set.
   * @return A Mono of CreditDto
   */
  @PutMapping("/limit/{creditId}/{limit}")
  @ResponseStatus(HttpStatus.OK)
  public Mono<CreditDto> updateCreditLimit(@PathVariable String creditId, @PathVariable Double limit) {
    log.info("update Credit ({}) limit: {}", creditId, limit);
    return creditService.updateCreditLimit(creditId, limit);
  }

}
