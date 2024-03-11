package org.example.entities;


import jakarta.persistence.*;
import lombok.Data;
import org.example.enums.ProductTypeEnum;

import java.sql.Date;

@Entity
@Data
@Table(schema = "springtask_tst_schema")
public class TppProduct {
  @Id
  @SequenceGenerator(name = "sequence", schema = "springtask_tst_schema", sequenceName = "tpp_product_id_seq", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence")
  private Long id;
  private Long productCodeId;
  private Long clientId;
  private ProductTypeEnum type;
  private String number;
  private Long priority;
  private Date dateOfConclusion;
  private Date startDateTime;
  private Date endDateTime;
  private Long days;
  private Double penaltyRate;
  private Double nso;
  private Double thresholdAmount;
  private String requisiteType;
  private String interestRateType;
  private Double taxRate;
  private String reasoneClose;
  private String state;
}
