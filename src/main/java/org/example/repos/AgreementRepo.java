package org.example.repos;

import org.example.entities.Agreement;
import org.springframework.data.repository.CrudRepository;

public interface AgreementRepo extends CrudRepository<Agreement, Long> {
    Agreement findAgreementByNumber(String number);
}
