package com.nttdata.microservices.credit.service.dto;

import com.nttdata.microservices.credit.entity.client.ClientType;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class ClientDto {

  private String id;

  private String documentNumber;

  private String firstNameBusiness;

  private String surnames;

  private ClientType clientType;
}
