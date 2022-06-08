package com.nttdata.microservices.credit.service.impl;

import com.nttdata.microservices.credit.entity.Credit;
import com.nttdata.microservices.credit.entity.client.ClientType;
import com.nttdata.microservices.credit.exception.DataValidationException;
import com.nttdata.microservices.credit.proxy.ClientProxy;
import com.nttdata.microservices.credit.repository.CreditRepository;
import com.nttdata.microservices.credit.service.CreditService;
import com.nttdata.microservices.credit.service.dto.CreditDto;
import com.nttdata.microservices.credit.service.mapper.CreditMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class CreditServiceImpl implements CreditService {

    private final CreditRepository creditRepository;

    private final ClientProxy clientProxy;

    private final CreditMapper creditMapper;

    /**
     * Find all credits and map them to a DTO
     *
     * @return Flux<CreditDto>
     */
    @Override
    public Flux<CreditDto> findAllCredits() {
        return creditRepository.findAllCredits();
    }

    @Override
    public Mono<CreditDto> findByCreditId(String creditId) {
        return creditRepository.findByCreditId(creditId);
    }

    @Override
    public Flux<CreditDto> findByClientId(String clientId) {
        return creditRepository.findByClientId(clientId);
    }

    @Override
    public Mono<CreditDto> findWithCardByCreditId(String creditId) {
        return creditRepository.findWithCardByCreditId(creditId);
    }

    /**
     * It returns a Flux of CreditDto objects, which are mapped from Credit objects,
     * which are found by
     * the client's document number.
     *
     * @param documentNumber String
     * @return A Flux of CreditDto
     */
    @Override
    public Flux<CreditDto> findByClientDocumentNumber(String documentNumber) {
        return creditRepository.findByClientDocumentNumber(documentNumber);
    }

    /**
     * Create credit
     *
     * @param creditDto the object that is passed to the method
     * @return Mono<CreditDto>
     */
    @Override
    public Mono<CreditDto> createCredit(CreditDto creditDto) {
        log.info("Create credit");

        return clientProxy.getClientByDocumentNumber(creditDto.getClientDocumentNumber())
                .switchIfEmpty(Mono.error(new DataValidationException("Client not found")))
                .doOnNext(creditDto::setClient)
                .then(creditRepository.findByAccountNumber(creditDto.getAccountNumber())
                        .flatMap(r -> Mono.error(new DataValidationException("Account number already has a credit")))
                        .then()
                )
                .then(creditRepository.findByClientDocumentNumber(creditDto.getClientDocumentNumber())
                        .count()
                        .<Long>handle((record, sink) -> {
                            if (record > 0 && creditDto.getClient().getClientType() == ClientType.PERSONAL) {
                                sink.error(new DataValidationException("Personal customer already has a credit"));
                            } else {
                                sink.complete();
                            }
                        })
                )
                .then(Mono.just(creditDto)
                        .flatMap(dto -> {
                            Credit credit = creditMapper.toEntity(dto);
                            credit.setRegisterDate(LocalDateTime.now());
                            return Mono.just(credit);
                        })
                        .flatMap(creditRepository::insert)
                        .map(creditMapper::toDto));
    }

    /**
     * Find the credit by id, then map the creditDto to an entity, then set the id
     * of the entity to the id of the credit, then save the entity, then map the entity to a dto.
     *
     * @param id           The id of the credit to be updated
     * @param creditAmount Amount to increase in the credit
     * @return A Mono of CreditDto
     */
    @Override
    public Mono<CreditDto> updateCreditAmount(String id, Double creditAmount) {
        return creditRepository.findById(id)
                .switchIfEmpty(Mono.error(new DataValidationException("Credit not found")))
                .map(credit -> {
                    double totalAmount = Double.sum(creditAmount, credit.getAmount());
                    credit.setAmount(totalAmount);
                    return credit;
                })
                .flatMap(creditRepository::save)
                .map(creditMapper::toDto);
    }

}
