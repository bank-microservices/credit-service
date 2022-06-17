package com.nttdata.microservices.credit.controller;

import com.nttdata.microservices.credit.service.CreditCardService;
import com.nttdata.microservices.credit.service.dto.CreditCardDto;
import com.nttdata.microservices.credit.util.ResponseUtil;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
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

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/credit/card")
public class CreditCardController {

  private final CreditCardService cardService;

  /**
   * This function returns a Flux of CreditCardDto objects, which is a list of all
   * the credit cards in the database.
   *
   * @return A Flux of CreditCardDto objects.
   */
  @GetMapping
  public Flux<CreditCardDto> getAll() {
    return cardService.findAll();
  }

  /**
   * It takes a credit card id, finds the credit card, and returns it as a
   * response entity
   *
   * @param id The id of the credit card to retrieve.
   * @return A Mono of a ResponseEntity of a CreditCardDto
   */
  @GetMapping("/{id}")
  public Mono<ResponseEntity<CreditCardDto>> findById(@PathVariable String id) {
    return cardService.findById(id)
        .map(ResponseEntity::ok)
        .defaultIfEmpty(ResponseEntity.notFound().build());
  }

  /**
   * It returns a Flux of CreditCardDto objects, which are the result of calling
   * the findByClientDocumentNumber function of the cardService object, passing
   * the number parameter as an argument
   *
   * @param number The number of the client's document.
   * @return A Flux of CreditCardDto
   */
  @GetMapping("/client/{number}")
  public Flux<CreditCardDto> findByClientDocumentNumber(@PathVariable String number) {
    return cardService.findByClientDocumentNumber(number);
  }

  /**
   * Find a credit by its id and return it with its card.
   *
   * @param accountNumber The
   * @return A Mono of CreditDto
   */
  @GetMapping("/account/{number}")
  public Mono<ResponseEntity<CreditCardDto>> findByAccountNumber(
      @PathVariable("number") String accountNumber) {
    Mono<CreditCardDto> dtoMono = cardService.findByAccountNumber(accountNumber);
    return ResponseUtil.wrapOrNotFound(dtoMono);
  }


  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public Mono<CreditCardDto> create(@Valid @RequestBody CreditCardDto customer) {
    return cardService.create(customer);
  }

  /**
   * If the card is found, update it and return a 200 OK response, otherwise
   * return a 404 Not Found
   * response.
   *
   * @param id       The id of the credit card to update
   * @param customer The object that will be updated.
   * @return A Mono of ResponseEntity of CreditCardDto
   */
  @PutMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  public Mono<ResponseEntity<CreditCardDto>> update(@PathVariable String id,
                                                    @Valid @RequestBody CreditCardDto customer) {
    return cardService.update(id, customer)
        .map(ResponseEntity::ok)
        .defaultIfEmpty(ResponseEntity.notFound().build());
  }

  /**
   * It takes a credit card id, a credit card object, and returns a Mono of a
   * ResponseEntity of a CreditCardDto
   *
   * @param id       The id of the credit card to update
   * @param customer The object that will be used to update the existing object.
   * @return A Mono<ResponseEntity<CreditCardDto>>
   */
  @PutMapping("/partial/{id}")
  @ResponseStatus(HttpStatus.OK)
  public Mono<ResponseEntity<CreditCardDto>> partialUpdate(@PathVariable String id,
                                                           @Valid @RequestBody
                                                           CreditCardDto customer) {
    return cardService.partialUpdate(id, customer)
        .map(ResponseEntity::ok)
        .defaultIfEmpty(ResponseEntity.notFound().build());
  }

  /**
   * It deletes a card from the database
   *
   * @param id The id of the card to be deleted.
   * @return A Mono<Void>
   */
  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public Mono<Void> delete(@PathVariable String id) {
    return cardService.delete(id);
  }

}
