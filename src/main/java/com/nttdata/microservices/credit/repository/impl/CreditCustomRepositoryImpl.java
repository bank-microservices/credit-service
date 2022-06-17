package com.nttdata.microservices.credit.repository.impl;

import com.nttdata.microservices.credit.entity.Credit;
import com.nttdata.microservices.credit.repository.CreditCustomRepository;
import com.nttdata.microservices.credit.service.dto.CreditDto;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.Fields;
import org.springframework.data.mongodb.core.aggregation.LookupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.UnwindOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class CreditCustomRepositoryImpl implements CreditCustomRepository {

  private final ReactiveMongoTemplate reactiveMongoTemplate;

  @Override
  public Flux<CreditDto> findAllCredits() {
    LookupOperation lookup = Aggregation.lookup("credit_card", "creditCardId", "_id", "creditCard");
    UnwindOperation unwindOperation = new UnwindOperation(Fields.field("$creditCard"), true);
    Aggregation aggregation = Aggregation.newAggregation(lookup, unwindOperation);
    return reactiveMongoTemplate.aggregate(aggregation, Credit.class, CreditDto.class);
  }

  @Override
  public Mono<CreditDto> findByCreditId(String creditId) {
    Aggregation aggregation = getAggregationCredit("_id", new ObjectId(creditId));
    return reactiveMongoTemplate.aggregate(aggregation, Credit.class, CreditDto.class)
        .singleOrEmpty();
  }

  @Override
  public Flux<CreditDto> findByClientId(String clientId) {
    Aggregation aggregation = getAggregationCredit("client._id", new ObjectId(clientId));
    return reactiveMongoTemplate.aggregate(aggregation, Credit.class, CreditDto.class);
  }

  @Override
  public Flux<CreditDto> findByClientDocumentNumber(String documentNumber) {
    Aggregation aggregation = getAggregationCredit("client.documentNumber", documentNumber);
    return reactiveMongoTemplate.aggregate(aggregation, Credit.class, CreditDto.class);
  }

  private Aggregation getAggregationCredit(String fieldCriteria, Object valueCriteria) {
    LookupOperation lookup = Aggregation.lookup("credit_card", "creditCardId", "_id", "creditCard");
    UnwindOperation unwindOperation = new UnwindOperation(Fields.field("$creditCard"), true);
    MatchOperation matchStage = Aggregation.match(Criteria.where(fieldCriteria).is(valueCriteria));
    return Aggregation.newAggregation(matchStage, lookup, unwindOperation);
  }

}
