package com.nttdata.microservices.credit.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.READ_ONLY;
import static com.fasterxml.jackson.annotation.JsonProperty.Access.WRITE_ONLY;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreditCardDto {

  @NotBlank(message = "accountNumber is required")
  @JsonProperty(access = WRITE_ONLY)
  private String accountNumber;

  @NotBlank(message = "clientDocumentNumber is required")
  @JsonProperty(access = WRITE_ONLY)
  private String clientDocumentNumber;

  @NotBlank(message = "cardNumber is required")
  private String cardNumber;

  @NotBlank(message = "cvv is required")
  private String cvv;

  @NotNull(message = "ExpirationDate is required")
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private LocalDate expirationDate;

  @JsonProperty(access = READ_ONLY)
  private String id;

  @JsonProperty(access = READ_ONLY)
  private ClientDto client;

  @JsonProperty(access = READ_ONLY)
  private AccountDto account;

  @JsonProperty(access = READ_ONLY)
  private boolean status;

}
