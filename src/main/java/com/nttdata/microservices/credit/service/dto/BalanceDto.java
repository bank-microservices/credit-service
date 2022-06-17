package com.nttdata.microservices.credit.service.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BalanceDto {

  private String accountNumber;
  private Double consumed;
  private Double available;
  private Double limit;

}
