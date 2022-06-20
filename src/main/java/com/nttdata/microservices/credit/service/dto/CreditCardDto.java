package com.nttdata.microservices.credit.service.dto;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.READ_ONLY;
import static com.fasterxml.jackson.annotation.JsonProperty.Access.WRITE_ONLY;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CreditCardDto {

  @NotBlank(message = "clientDocumentNumber is required")
  @JsonProperty(access = WRITE_ONLY)
  private String clientDocumentNumber;

  @NotBlank(message = "cardNumber is required")
  private String cardNumber;

  @NotBlank(message = "cvv is required")
  private String cvv;

  @NotBlank(message = "accountNumber is required")
  private String accountNumber;

  @NotNull(message = "expirationDate is required")
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private LocalDate expirationDate;

  @NotNull(message = "cci is required")
  private String cci;

  @NotNull(message = "creditLimit is required")
  @Positive(message = "creditLimit must be greater than zero (0)")
  private Double creditLimit;

  @JsonProperty(access = READ_ONLY)
  private String id;

  @JsonProperty(access = READ_ONLY)
  private Double amount;

  @JsonProperty(access = READ_ONLY)
  private ClientDto client;

  @JsonProperty(access = READ_ONLY)
  private Boolean status;

}
