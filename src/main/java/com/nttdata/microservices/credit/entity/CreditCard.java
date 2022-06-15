package com.nttdata.microservices.credit.entity;

import com.nttdata.microservices.credit.entity.account.Account;
import com.nttdata.microservices.credit.entity.client.Client;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Data
@Document(collection = "credit_card")
@AllArgsConstructor
@NoArgsConstructor
public class CreditCard extends AbstractAuditingEntity {

  @Id
  private String id;

  @Indexed(unique = true)
  private String cardNumber;
  private String cvv;
  private Client client;
  private Account account;
  private LocalDate expirationDate;
  private boolean status = true;

}
