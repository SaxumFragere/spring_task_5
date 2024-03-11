package org.example.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(schema = "springtask_tst_schema")
public class TppRefProductClass {
  @Id
  private Long internalId;
  private String value;
  private String gbiCode;
  private String gbiName;
  private String productRowCode;
  private String productRowName;
  private String subclassCode;
  private String subclassName;

}
