package com.nttdata.microservices.credit.repository.impl;

import com.nttdata.microservices.credit.entity.CreditCard;
import com.nttdata.microservices.credit.repository.CreditCardCustomRepository;
import com.nttdata.microservices.credit.service.dto.CreditCardDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class CreditCardCustomRepositoryImpl implements CreditCardCustomRepository {

  private final ReactiveMongoTemplate reactiveMongoTemplate;

  @Override
  public Mono<CreditCard> findByAccountNumber(String accountNumber) {
    MatchOperation matchStage = Aggregation.match(Criteria
        .where("accountNumber").is(accountNumber));
    Aggregation aggregation = Aggregation.newAggregation(matchStage);
    return reactiveMongoTemplate.aggregate(aggregation, CreditCard.class, CreditCard.class)
        .singleOrEmpty();
  }

  @Override
  public Flux<CreditCard> findByClientDocumentNumber(String documentNumber) {
    MatchOperation matchStage = Aggregation.match(Criteria
        .where("client.documentNumber").is(documentNumber));
    Aggregation aggregation = Aggregation.newAggregation(matchStage);
    return reactiveMongoTemplate.aggregate(aggregation, CreditCard.class, CreditCard.class);
  }

  @Override
  public Mono<CreditCard> findByClientDocumentAndCardNumber(String documentNumber,
                                                               String cardNumber) {
    Criteria criteria = Criteria.where("client.documentNumber").is(documentNumber)
        .and("cardNumber").is(cardNumber);
    MatchOperation matchStage = Aggregation.match(criteria);
    Aggregation aggregation = Aggregation.newAggregation(matchStage);
    return reactiveMongoTemplate.aggregate(aggregation, CreditCard.class, CreditCard.class)
        .singleOrEmpty();
  }
}
