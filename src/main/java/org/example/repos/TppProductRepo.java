package org.example.repos;

import org.example.entities.TppProduct;
import org.springframework.data.repository.CrudRepository;

public interface TppProductRepo extends CrudRepository<TppProduct, Long> {
    TppProduct findByNumber(String number);
}
