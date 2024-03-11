package org.example.corporateSettlementAccount.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.example.corporateSettlementAccount.dto.CorporateSettlementAccountDTO;
import org.example.entities.Account;
import org.example.entities.AccountPool;
import org.example.entities.TppProductRegister;
import org.example.entities.TppRefProductRegisterType;
import org.example.enums.ProductRegisterStateEnum;
import org.example.repos.AccountPoolRepo;
import org.example.repos.AccountRepo;
import org.example.repos.TppProductRegisterRepo;
import org.example.repos.TppRefProductRegisterTypeRepo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.json.Json;

@Component
@RestController
@RequestMapping(value = "/corporate-settlement-account")
@AllArgsConstructor
public class CorporateSettlementAccount {

    TppProductRegisterRepo tppProductRegisterRepo;
    TppRefProductRegisterTypeRepo tppRefProductRegisterTypeRepo;
    AccountPoolRepo accountPoolRepo;
    AccountRepo accountRepo;

    @PostMapping("/create")
    public ResponseEntity<String> create(@Valid @RequestBody CorporateSettlementAccountDTO corporateSettlementAccountDTO) {

        TppProductRegister tppProductRegister = tppProductRegisterRepo.findByProductIdAndType(corporateSettlementAccountDTO.getInstanceId(), corporateSettlementAccountDTO.getRegistryTypeCode());
        if (tppProductRegister != null){
            String response = "Параметр registryTypeCode тип регистра <" +
                    tppProductRegister.getType() +
                    "> уже существует для ЭП с ИД  <" +
                    tppProductRegister.getProductId() +
                    ">.";
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        TppRefProductRegisterType tppRefProductRegisterType = tppRefProductRegisterTypeRepo.findByValue(corporateSettlementAccountDTO.getRegistryTypeCode());
        if (tppRefProductRegisterType == null){
            String response = "Код Продукта <" +
                    corporateSettlementAccountDTO.getRegistryTypeCode() +
                    "> не найдено в Каталоге продуктов <" +
                    "springtask_tst_schema.tpp_ref_product_register_type" +
                    "> для данного типа Регистра";
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        Account account = findAccount(corporateSettlementAccountDTO);
        saveTppProductRegister(corporateSettlementAccountDTO, account);

        return new ResponseEntity<>(buildOkResponse(account), HttpStatus.OK);
    }

    private Account findAccount(CorporateSettlementAccountDTO corporateSettlementAccountDTO){
        AccountPool accountPool = accountPoolRepo.findAccountPoolByAllReq(
                corporateSettlementAccountDTO.getBranchCode(),
                corporateSettlementAccountDTO.getCurrencyCode(),
                corporateSettlementAccountDTO.getMdmCode(),
                corporateSettlementAccountDTO.getPriorityCode(),
                corporateSettlementAccountDTO.getRegistryTypeCode());

        return accountRepo.findFirstByAccountPoolIdOrderById(accountPool.getId());
    }

    private void saveTppProductRegister(CorporateSettlementAccountDTO corporateSettlementAccountDTO, Account account){
        TppProductRegister tppProductRegister = new TppProductRegister();
        tppProductRegister.setProductId(corporateSettlementAccountDTO.getInstanceId());
        tppProductRegister.setType(corporateSettlementAccountDTO.getRegistryTypeCode());
        tppProductRegister.setAccount(account.getId());
        tppProductRegister.setCurrencyCode(corporateSettlementAccountDTO.getCurrencyCode());
        tppProductRegister.setAccountNumber(account.getAccountNumber());
        tppProductRegister.setState(String.valueOf(ProductRegisterStateEnum.OPEN));

        tppProductRegisterRepo.save(tppProductRegister);
    }

    private String buildOkResponse(Account account){
        return Json.createObjectBuilder()
                .add("data", Json.createObjectBuilder().add("accountId", account.getId()))
                .build().toString();
    }
}
