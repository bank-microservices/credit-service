package com.nttdata.microservices.credit.entity.client;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.ReadOnlyProperty;

@Data
@NoArgsConstructor
public class Client {
  private String id;

  private String documentNumber;

  @ReadOnlyProperty
  private String firstNameBusiness;

  @ReadOnlyProperty
  private String surnames;

  private ClientType clientType;
}
