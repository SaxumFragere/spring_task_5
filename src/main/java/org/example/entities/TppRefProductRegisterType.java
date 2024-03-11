package org.example.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.util.Date;

@Entity
@Data
@Table(schema = "springtask_tst_schema")
public class TppRefProductRegisterType {
    @Id
    Long internalId;
    String value;
    String registerTypeName;
    String productClassCode;
    Date registerTypeStartDate;
    Date registerTypeEndDate;
    String accountType;
}
