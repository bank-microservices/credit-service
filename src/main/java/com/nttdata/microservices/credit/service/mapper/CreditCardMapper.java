package com.nttdata.microservices.credit.service.mapper;

import com.nttdata.microservices.credit.entity.CreditCard;
import com.nttdata.microservices.credit.entity.client.Client;
import com.nttdata.microservices.credit.service.dto.ClientDto;
import com.nttdata.microservices.credit.service.dto.CreditCardDto;
import com.nttdata.microservices.credit.service.mapper.base.EntityMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface CreditCardMapper extends EntityMapper<CreditCardDto, CreditCard> {

  @Mapping(target = "client", source = "client", qualifiedByName = "cardClient")
  CreditCardDto toDto(CreditCard entity);

  @Named("cardClient")
  ClientDto toClientDto(Client client);


}
