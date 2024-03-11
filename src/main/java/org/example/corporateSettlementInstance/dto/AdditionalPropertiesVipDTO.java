package org.example.corporateSettlementInstance.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AdditionalPropertiesVipDTO {
    String key;
    String value;
    String name;
}
