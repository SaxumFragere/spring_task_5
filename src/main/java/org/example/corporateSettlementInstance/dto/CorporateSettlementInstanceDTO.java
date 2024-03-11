package org.example.corporateSettlementInstance.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import org.example.entities.Agreement;
import org.example.enums.ProductTypeEnum;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class CorporateSettlementInstanceDTO {
    Long instanceId;
    @NotNull(message = "productType may not be empty")
    ProductTypeEnum productType;
    @NotBlank(message = "productCode may not be empty")
    String productCode;
    @NotBlank(message = "registerType may not be empty")
    String registerType;
    @NotBlank(message = "mdmCode may not be empty")
    String mdmCode;
    @NotBlank(message = "contractNumber may not be empty")
    String contractNumber;
    @NotNull(message = "contractDate may not be empty")
    Date contractDate;
    @NotNull(message = "priority may not be empty")
    Long priority;
    Double interestRatePenalty;
    Double minimalBalance;
    Double thresholdAmount;
    String accountingDetails;
    String rateType;
    Double taxPercentageRate;
    Double technicalOverdraftLimitAmount;
    @NotNull(message = "contractId may not be empty")
    Integer contractId;
    @NotBlank(message = "branchCode may not be empty")
    String branchCode;
    @NotBlank(message = "isoCurrencyCode may not be empty")
    String isoCurrencyCode;
    @NotBlank(message = "urgencyCode may not be empty")
    String urgencyCode;
    @NotNull(message = "referenceCode may not be empty")
    Integer referenceCode;
    List<AdditionalPropertiesVipDTO> additionalPropertiesVip;
    @NotNull(message = "agreements may not be empty")
    List<Agreement> agreements;
}
