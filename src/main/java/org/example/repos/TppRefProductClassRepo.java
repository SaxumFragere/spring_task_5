package org.example.repos;

import org.example.entities.TppRefProductClass;
import org.springframework.data.repository.CrudRepository;

public interface TppRefProductClassRepo extends CrudRepository<TppRefProductClass, Long> {
    TppRefProductClass findByValue(String value);
}
