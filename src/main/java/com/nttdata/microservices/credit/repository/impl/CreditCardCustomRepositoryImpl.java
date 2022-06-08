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

@RequiredArgsConstructor
public class CreditCardCustomRepositoryImpl implements CreditCardCustomRepository {

    private final ReactiveMongoTemplate reactiveMongoTemplate;

    @Override
    public Flux<CreditCardDto> findByClientDocumentNumber(String documentNumber) {
        MatchOperation matchStage = Aggregation.match(Criteria.where("client.documentNumber").is(documentNumber));
        Aggregation aggregation = Aggregation.newAggregation(matchStage);
        return reactiveMongoTemplate.aggregate(aggregation, CreditCard.class, CreditCardDto.class);
    }
}
