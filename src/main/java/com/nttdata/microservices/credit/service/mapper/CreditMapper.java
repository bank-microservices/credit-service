package com.nttdata.microservices.credit.service.mapper;

import com.nttdata.microservices.credit.entity.Credit;
import com.nttdata.microservices.credit.entity.client.Client;
import com.nttdata.microservices.credit.service.dto.ClientDto;
import com.nttdata.microservices.credit.service.dto.CreditDto;
import com.nttdata.microservices.credit.service.mapper.base.EntityMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface CreditMapper extends EntityMapper<CreditDto, Credit> {

  @Mapping(target = "client", source = "client", qualifiedByName = "creditClient")
  CreditDto toDto(Credit entity);

  @Named("creditClient")
  ClientDto toDtoClient(Client client);

}
