package com.nttdata.microservices.credit.entity;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(collection = "credit_card")
public class CreditCard extends Product {

  @Id
  private String id;

  @Indexed(unique = true)
  private String cardNumber;
  private String cvv;
  private LocalDate expirationDate;

}
