package org.example.repos;

import org.example.entities.TppRefProductRegisterType;
import org.springframework.data.repository.CrudRepository;

public interface TppRefProductRegisterTypeRepo extends CrudRepository<TppRefProductRegisterType, Long> {
    TppRefProductRegisterType findByValue(String value);
}
