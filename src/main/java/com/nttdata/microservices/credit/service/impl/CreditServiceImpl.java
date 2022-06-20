package com.nttdata.microservices.credit.service.impl;

import static com.nttdata.microservices.credit.util.MessageUtils.getMsg;

import com.nttdata.microservices.credit.entity.Credit;
import com.nttdata.microservices.credit.entity.client.ClientType;
import com.nttdata.microservices.credit.exception.BadRequestException;
import com.nttdata.microservices.credit.exception.CreditNotFoundException;
import com.nttdata.microservices.credit.proxy.ClientProxy;
import com.nttdata.microservices.credit.repository.CreditRepository;
import com.nttdata.microservices.credit.service.CreditService;
import com.nttdata.microservices.credit.service.dto.CreditDto;
import com.nttdata.microservices.credit.service.mapper.CreditMapper;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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

  /**
   * Find a credit by account number and map it to a creditDto
   *
   * @param accountNumber String
   * @return Mono<CreditDto>
   */
  @Override
  public Mono<CreditDto> findByAccountNumber(String accountNumber) {
    return creditRepository.findByAccountNumber(accountNumber)
        .map(creditMapper::toDto);
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
        .switchIfEmpty(Mono.error(new BadRequestException(getMsg("client.not.found"))))
        .doOnNext(creditDto::setClient)
        .then(findByAccountNumber(creditDto.getAccountNumber())
            .flatMap(r -> Mono.error(new BadRequestException(getMsg("account.number.already"))))
            .then())
        .then(findByClientDocumentNumber(creditDto.getClientDocumentNumber())
            .count()
            .<Long>handle((item, sink) -> {
              if (item > 0 && creditDto.getClient().getClientType() == ClientType.PERSONAL) {
                sink.error(new BadRequestException(getMsg("client.personal.already")));
              } else {
                sink.complete();
              }
            }))
        .then(Mono.just(creditDto)
            .flatMap(dto -> {
              Credit credit = creditMapper.toEntity(dto);
              credit.setRegisterDate(LocalDateTime.now());
              credit.setStatus(true);
              return Mono.just(credit);
            })
            .flatMap(creditRepository::insert)
            .map(creditMapper::toDto));
  }

  /**
   * Find the credit by id, then map the creditDto to an entity, then set the id
   * of the entity to the id of the credit, then save the entity, then map the
   * entity to a dto.
   *
   * @param id           The id of the credit to be updated
   * @param creditAmount Amount to increase in the credit
   * @return A Mono of CreditDto
   */
  @Override
  public Mono<CreditDto> updateCreditAmount(String id, Double creditAmount) {
    return creditRepository.findById(id)
        .switchIfEmpty(Mono.error(new CreditNotFoundException(getMsg("credit.not.found"))))
        .map(credit -> {
          double totalAmount = Double.sum(creditAmount, credit.getAmount());
          if (totalAmount > credit.getCreditLimit()) {
            throw new BadRequestException(getMsg("credit.exceed.limit"));
          } else {
            credit.setAmount(totalAmount);
            credit.setLastModifiedDate(LocalDateTime.now());
            return credit;
          }
        })
        .flatMap(creditRepository::save)
        .map(creditMapper::toDto);
  }

  /**
   * Search for a credit by id, if it does not exist it throws an error and checks
   * if the new credit limit is less than the amount of the current credit, if so
   * it throws an error, otherwise it updates the credit limit
   *
   * @param id          credit id
   * @param creditLimit The new credit limit
   * @return A Mono of CreditDto
   */
  @Override
  public Mono<CreditDto> updateCreditLimit(String id, Double creditLimit) {
    return creditRepository.findById(id)
        .switchIfEmpty(Mono.error(new CreditNotFoundException(getMsg("credit.not.found"))))
        .map(credit -> {
          if (credit.getAmount() > creditLimit) {
            throw new BadRequestException(getMsg("credit.less.limit"));
          } else {
            credit.setCreditLimit(creditLimit);
            credit.setLastModifiedDate(LocalDateTime.now());
            return credit;
          }
        })
        .flatMap(creditRepository::save)
        .map(creditMapper::toDto);
  }

}
