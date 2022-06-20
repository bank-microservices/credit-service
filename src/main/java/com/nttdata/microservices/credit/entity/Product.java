package com.nttdata.microservices.credit.entity;

import com.nttdata.microservices.credit.entity.client.Client;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class Product extends AbstractAuditingEntity {

  private String accountNumber;
  private String cci;
  private Double creditLimit;
  private Double amount;
  private Client client;
  private Boolean status;

}
