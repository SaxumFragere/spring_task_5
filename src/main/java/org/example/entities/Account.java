package org.example.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(schema = "springtask_tst_schema")
public class Account {
  @Id
  private Long id;
  private Long accountPoolId;
  private String accountNumber;
  private boolean bussy;

}
