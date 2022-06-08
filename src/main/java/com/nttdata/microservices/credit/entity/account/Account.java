package com.nttdata.microservices.credit.entity.account;

import lombok.Data;
import org.springframework.data.annotation.ReadOnlyProperty;

@Data
public class Account {
    private String id;
    private String accountNumber;

    @ReadOnlyProperty
    private String cci;

    @ReadOnlyProperty
    private Double maintenanceFee;

    @ReadOnlyProperty
    private Integer maxLimitMonthlyMovements;

    private AccountType accountType;
}
