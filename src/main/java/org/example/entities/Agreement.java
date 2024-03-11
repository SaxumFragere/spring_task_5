package org.example.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.sql.Date;

@Entity
@Data
@Table(schema = "springtask_tst_schema")
public class Agreement {
  @Id
  @SequenceGenerator(name = "sequence", schema = "springtask_tst_schema", sequenceName = "agreement_id_seq", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence")
  private Long id;
  private Long productId;
  private String generalAgreementId;
  private String supplementaryAgreementId;
  private String arrangementType;
  private Long shedulerJobId;
  @NotBlank(message = "number may not be empty")
  private String number;
  @NotNull(message = "openingDate may not be empty")
  private Date openingDate;
  private Date closingDate;
  private Date cancelDate;
  private Long validityDuration;
  private String cancellationReason;
  private String status;
  private Date interestCalculationDate;
  private Long interestRate;
  private Long coefficient;
  private String coefficientAction;
  private Long minimumInterestRate;
  private Long minimumInterestRateCoefficient;
  private String minimumInterestRateCoefficientAction;
  private Long maximalInterestRate;
  private Long maximalInterestRateCoefficient;
  private String maximalInterestRateCoefficientAction;
}
