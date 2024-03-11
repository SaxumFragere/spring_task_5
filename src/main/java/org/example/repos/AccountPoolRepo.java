package org.example.repos;

import org.example.entities.AccountPool;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface AccountPoolRepo extends CrudRepository<AccountPool, Long> {
    @Query("select p " +
            "from AccountPool p " +
            "where p.branchCode = :branchCode " +
            "and p.currencyCode = :currencyCode " +
            "and p.mdmCode = :mdmCode " +
            "and p.priorityCode = :priorityCode " +
            "and p.registryTypeCode = :registryTypeCode")
    AccountPool findAccountPoolByAllReq(String branchCode, String currencyCode, String mdmCode, String priorityCode, String registryTypeCode);
}
