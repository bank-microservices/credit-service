package com.nttdata.microservices.credit.entity;

import com.nttdata.microservices.credit.entity.client.Client;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document(collection = "credit")
@AllArgsConstructor
@NoArgsConstructor
public class Credit extends AbstractAuditingEntity {

    @Id
    private String id;
    private String accountNumber;
    private String cci;
    private double creditLimit;
    private double amount;
    private Client client;
    private LocalDateTime registerDate;
    private boolean status = true;
}
