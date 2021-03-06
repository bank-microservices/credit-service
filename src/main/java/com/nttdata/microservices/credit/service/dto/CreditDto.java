package com.nttdata.microservices.credit.service.dto;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.READ_ONLY;
import static com.fasterxml.jackson.annotation.JsonProperty.Access.WRITE_ONLY;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class CreditDto {

  @NotBlank(message = "clientDocumentNumber is required")
  @JsonProperty(access = WRITE_ONLY)
  private String clientDocumentNumber;

  @NotNull(message = "accountNumber is required")
  private String accountNumber;

  @NotNull(message = "cci is required")
  private String cci;

  @NotNull(message = "creditLimit is required")
  @Positive(message = "creditLimit must be greater than zero (0)")
  private Double creditLimit;

  @NotNull(message = "amount is required")
  @Positive(message = "amount must be greater than zero (0)")
  private Double amount;

  @JsonProperty(access = READ_ONLY)
  private String id;

  @JsonProperty(access = READ_ONLY)
  private LocalDateTime registerDate;

  @JsonProperty(access = READ_ONLY)
  private ClientDto client;

  @JsonProperty(access = READ_ONLY)
  private Boolean status;
}
