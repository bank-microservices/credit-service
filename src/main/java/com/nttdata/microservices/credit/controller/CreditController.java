package com.nttdata.microservices.credit.controller;

import com.nttdata.microservices.credit.service.CreditService;
import com.nttdata.microservices.credit.service.dto.CreditDto;
import com.nttdata.microservices.credit.util.ResponseUtil;
import lombok.RequiredArgsConstructor;
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
        return creditService.findAllCredits();
    }

    /**
     * This returns the credit related to a specific client
     * 
     * @param documentNumber The document number of the client.
     * @return A Flux of CreditDto
     */
    @GetMapping("/client/{documentNumber}")
    public Flux<CreditDto> findNumber(@PathVariable("documentNumber") String documentNumber) {
        return creditService.findByClientDocumentNumber(documentNumber);
    }

    /**
     * Find a credit by its id and return it with its card.
     * 
     * @param creditId The credit ID
     * @return A Mono of CreditDto
     */
    @GetMapping("/{creditId}/card")
    public Mono<ResponseEntity<CreditDto>> findWithCardByCreditId(@PathVariable("creditId") String creditId) {
        Mono<CreditDto> dtoMono = creditService.findWithCardByCreditId(creditId);
        return ResponseUtil.wrapOrNotFound(dtoMono);
    }

    /**
     * It returns a Mono of a ResponseEntity of a CreditDto
     * 
     * @param id The id of the credit to be retrieved.
     * @return A Mono<ResponseEntity<CreditDto>>
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<CreditDto>> findByCreditId(@PathVariable("id") String id) {
        Mono<CreditDto> dtoMono = creditService.findByCreditId(id);
        return ResponseUtil.wrapOrNotFound(dtoMono);
    }

    /**
     * It returns a Mono of a ResponseEntity of a CreditDto
     * 
     * @param clientId The credit to be created.
     * @return A Mono<ResponseEntity<CreditDto>>
     */
    @GetMapping("/client/{clientId}")
    public Flux<CreditDto> findByClientId(@PathVariable("clientId") String clientId) {
        return creditService.findByClientId(clientId);
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
        return creditService.createCredit(creditDto);
    }

    @PutMapping("/amount/{id}/{amount}")
    @ResponseStatus(HttpStatus.OK)
    private Mono<CreditDto> updateAmount(@PathVariable String id, @PathVariable Double amount) {
        return creditService.updateCreditAmount(id, amount);
    }

}
