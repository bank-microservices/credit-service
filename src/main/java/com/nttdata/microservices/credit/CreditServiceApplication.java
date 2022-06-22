package com.nttdata.microservices.credit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@SpringBootApplication
@EnableMongoAuditing
@EnableEurekaClient
public class CreditServiceApplication {

  public static void main(String[] args) {
    SpringApplication.run(CreditServiceApplication.class, args);
  }

}
