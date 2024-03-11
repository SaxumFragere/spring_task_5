package org.example.repos;

import org.example.entities.Account;
import org.springframework.data.repository.CrudRepository;

public interface AccountRepo extends CrudRepository<Account, Long> {
    Account findFirstByAccountPoolIdOrderById(long accountPoolId);
}