package com.nttdata.microservices.credit.service.dto;

import com.nttdata.microservices.credit.entity.account.AccountType;
import lombok.Data;

@Data
public class AccountDto {
  private String id;
  private String accountNumber;
  private String cci;
  private Double maintenanceFee;
  private Integer maxLimitMonthlyMovements;
  private AccountType accountType;
}
