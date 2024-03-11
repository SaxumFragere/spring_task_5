package org.example.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Data
@Table(schema = "springtask_tst_schema")
public class TppRefAccountType {
    @Id
    Long internalId;
    String value;
}
