package org.example.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Table(schema = "springtask_tst_schema")
public class TppProductRegister {
    @Id
    @SequenceGenerator(name = "sequence", schema = "springtask_tst_schema", sequenceName = "tpp_product_register_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence")
    private Long id;
    private Long productId;
    private String type;
    private Long account;
    private String currencyCode;
    private String state;
    private String accountNumber;
}
