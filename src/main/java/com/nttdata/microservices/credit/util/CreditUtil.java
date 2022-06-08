package com.nttdata.microservices.credit.util;

import reactor.core.publisher.Mono;

public class CreditUtil {

    public static Mono<Double> calculateCreditAvailable(Mono<Double> totalConsumption, Mono<Double> totalPayment){
        return totalConsumption
                .map(cred -> cred * -1d).log()
                .concatWith(totalPayment).log()
                .reduce(0d, Double::sum).log();
    }
}
