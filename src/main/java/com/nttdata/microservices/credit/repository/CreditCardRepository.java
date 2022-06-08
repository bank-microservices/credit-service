package com.nttdata.microservices.credit.repository;

import com.nttdata.microservices.credit.entity.CreditCard;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface CreditCardRepository extends ReactiveMongoRepository<CreditCard, String>, CreditCardCustomRepository {

    Mono<CreditCard> findByCardNumber(String cardNumber);

}
