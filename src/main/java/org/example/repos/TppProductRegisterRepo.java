package org.example.repos;

import org.example.entities.TppProductRegister;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TppProductRegisterRepo extends CrudRepository<TppProductRegister, Long> {
    TppProductRegister findByProductIdAndType(long productId, String type);
    List<TppProductRegister> findTppProductRegisterByProductId(long productId);
}
