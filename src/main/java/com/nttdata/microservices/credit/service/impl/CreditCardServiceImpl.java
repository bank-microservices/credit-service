package com.nttdata.microservices.credit.service.impl;

import com.nttdata.microservices.credit.exception.BadRequestException;
import com.nttdata.microservices.credit.exception.ClientNotFoundException;
import com.nttdata.microservices.credit.exception.CreditCardNotFoundException;
import com.nttdata.microservices.credit.proxy.AccountProxy;
import com.nttdata.microservices.credit.proxy.ClientProxy;
import com.nttdata.microservices.credit.repository.CreditCardRepository;
import com.nttdata.microservices.credit.service.CreditCardService;
import com.nttdata.microservices.credit.service.dto.CreditCardDto;
import com.nttdata.microservices.credit.service.mapper.CreditCardMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Slf4j
@Service
@RequiredArgsConstructor
public class CreditCardServiceImpl implements CreditCardService {

    private final CreditCardRepository cardRepository;

    private final ClientProxy clientProxy;
    private final AccountProxy accountProxy;

    private final CreditCardMapper cardMapper;

    /**
     * Find a credit card by id and return a Mono of CreditCardDto
     *
     * @param id The id of the credit card to be retrieved.
     * @return A Mono of CreditCardDto
     */
    @Override
    public Mono<CreditCardDto> findById(String id) {
        log.debug("Request to get Credit Card by Id: {}", id);
        return cardRepository.findById(id)
                .map(cardMapper::toDto);
    }

    /**
     * This function returns a Flux of CreditCardDto objects, which is a stream of
     * CreditCardDto objects.
     *
     * @return Flux<CreditCardDto>
     */
    @Override
    public Flux<CreditCardDto> findAll() {
        log.debug("Request to get all Credit Cards");
        return cardRepository.findAll()
                .map(cardMapper::toDto);
    }

    /**
     * Find a credit with a card by credit id.
     *
     * @param accountNumber the accountNumber of the credit
     * @return Mono<CreditDto>
     */
    @Override
    public Mono<CreditCardDto> findByAccountNumber(String accountNumber) {
        log.debug("Request to get Credit Card by accountNumber: {}", accountNumber);
        return cardRepository.findByAccountNumber(accountNumber);
    }

    @Override
    public Flux<CreditCardDto> findByClientDocumentNumber(String documentNumber) {
        log.debug("Request to get Credit Card by Client documentNumber: {}", documentNumber);
        return cardRepository.findByClientDocumentNumber(documentNumber);
    }

    @Override
    public Mono<CreditCardDto> findByCardNumber(String cardNumber) {
        log.debug("Request to create Credit Card : {}", cardNumber);
        return cardRepository.findByCardNumber(cardNumber)
                .flatMap(card -> accountProxy.findByAccountNumber(card.getAccount().getAccountNumber())
                        .flatMap(account -> {
                            card.setAccount(cardMapper.toAccountEntity(account));
                            return Mono.just(card);
                        }))
                .map(cardMapper::toDto);
    }

    /**
     * It creates a new credit card and returns it
     *
     * @param cardDto The object that will be saved in the database.
     * @return A Mono of CreditCardDto
     */
    @Override
    public Mono<CreditCardDto> create(CreditCardDto cardDto) {
        log.debug("Request to create Credit Card : {}", cardDto);
        return cardRepository.findByCardNumber(cardDto.getCardNumber())
                .flatMap(r -> Mono.error(new BadRequestException("Card number already exist")))
                .then(existClient(cardDto))
                .then(existAccountByClient(cardDto))
                .map(cardMapper::toEntity)
                .map(entity -> {
                    entity.setStatus(true);
                    return entity;
                })
                .flatMap(cardRepository::insert)
                .map(cardMapper::toDto)
                .subscribeOn(Schedulers.boundedElastic());
    }

    private Mono<CreditCardDto> existClient(CreditCardDto cardDto) {
        log.debug("Request to proxy Client by documentNumber: {}", cardDto.getClientDocumentNumber());
        return clientProxy.getClientByDocumentNumber(cardDto.getClientDocumentNumber())
                .switchIfEmpty(Mono.error(new ClientNotFoundException("Client not found")))
                .doOnNext(cardDto::setClient)
                .thenReturn(cardDto);
    }

    private Mono<CreditCardDto> existAccountByClient(CreditCardDto cardDto) {
        log.debug("Request to proxy Account by accountNumber: {} and documentNumber: {}", cardDto.getAccountNumber(), cardDto.getClientDocumentNumber());
        return accountProxy.findByAccountNumberAndClientDocument(cardDto.getAccountNumber(), cardDto.getClientDocumentNumber())
                .switchIfEmpty(Mono.error(new CreditCardNotFoundException("Account not found")))
                .singleOrEmpty()
                .doOnNext(cardDto::setAccount)
                .thenReturn(cardDto);
    }

    /**
     * "If the cardDto is valid, then save it to the database and return the saved
     * cardDto."
     *
     * @param cardDto The CreditCardDto object that is passed in from the
     *                controller.
     * @return A Mono of CreditCardDto
     */
    @Override
    public Mono<CreditCardDto> update(String id, CreditCardDto cardDto) {
        log.debug("Request to save Credit Card : {}", cardDto);
        return cardRepository.findById(id)
                .flatMap(p -> Mono.just(cardDto)
                        .map(cardMapper::toEntity)
                        .doOnNext(e -> e.setId(id)))
                .flatMap(this.cardRepository::save)
                .map(cardMapper::toDto);
    }

    /**
     * Find a card by id, map it to a new card, update the new card with the partial
     * update, save the new card, and map it to a dto.
     *
     * @param cardDto The DTO object that contains the data to be updated.
     * @return A Mono of CreditCardDto
     */
    @Override
    public Mono<CreditCardDto> partialUpdate(String id, CreditCardDto cardDto) {
        log.debug("Request to partially update Credit Card : {}", cardDto);
        return cardRepository
                .findById(id)
                .map(existingCard -> {
                    cardMapper.partialUpdate(existingCard, cardDto);
                    return existingCard;
                })
                .flatMap(cardRepository::save)
                .map(cardMapper::toDto);
    }

    /**
     * Delete the credit card with the given id.
     *
     * @param id The id of the credit card to delete.
     * @return A Mono of Void.
     */
    @Override
    public Mono<Void> delete(String id) {
        log.debug("Request to delete Credit Card : {}", id);
        return cardRepository.findById(id).flatMap(card -> {
            card.setStatus(false);
            cardRepository.save(card);
            return Mono.empty();
        });
    }
}
