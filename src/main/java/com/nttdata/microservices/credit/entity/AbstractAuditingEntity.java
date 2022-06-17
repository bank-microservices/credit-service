package com.nttdata.microservices.credit.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
public class AbstractAuditingEntity implements Serializable {

  @Field("created_by")
  @JsonIgnore
  private String createdBy;

  @CreatedDate
  @Field("created_date")
  @JsonIgnore
  private LocalDateTime createdDate = LocalDateTime.now();

  @Field("last_modified_by")
  @JsonIgnore
  private String lastModifiedBy;

  @LastModifiedDate
  @Field("last_modified_date")
  @JsonIgnore
  private LocalDateTime lastModifiedDate = LocalDateTime.now();

}
