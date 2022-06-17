package com.nttdata.microservices.credit.repository;

import com.nttdata.microservices.credit.entity.Credit;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface CreditRepository
    extends ReactiveMongoRepository<Credit, String>, CreditCustomRepository {

  Mono<Credit> findByAccountNumber(String accountNumber);

}
