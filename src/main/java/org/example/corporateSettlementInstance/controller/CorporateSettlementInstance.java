package org.example.corporateSettlementInstance.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.example.corporateSettlementInstance.dto.CorporateSettlementInstanceDTO;
import org.example.entities.*;
import org.example.enums.ProductRegisterStateEnum;
import org.example.repos.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@RestController
@RequestMapping(value = "/corporate-settlement-instance")
@AllArgsConstructor
public class CorporateSettlementInstance {

    TppProductRepo tppProductRepo;
    AgreementRepo agreementRepo;
    TppRefProductClassRepo tppRefProductClassRepo;
    AccountPoolRepo accountPoolRepo;
    AccountRepo accountRepo;
    TppProductRegisterRepo tppProductRegisterRepo;

    @PostMapping("/create")
    public ResponseEntity<String> create(@Valid @RequestBody CorporateSettlementInstanceDTO corporateSettlementInstanceDTO) {

        ResponseEntity<String> response;

        if (corporateSettlementInstanceDTO.getInstanceId() == null) {
            response = createNewInstance(corporateSettlementInstanceDTO);
        } else {
            response = updateInstance(corporateSettlementInstanceDTO);
        }

        return response;
    }

    private ResponseEntity<String> createNewInstance(CorporateSettlementInstanceDTO corporateSettlementInstanceDTO){

        TppProduct tppProduct = tppProductRepo.findByNumber(corporateSettlementInstanceDTO.getContractNumber());
        if (tppProduct != null) {
            String response = "Параметр ContractNumber № договора <"+tppProduct.getNumber()+"> уже существует.";
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        for(var agr : corporateSettlementInstanceDTO.getAgreements()){
            Agreement agreement = agreementRepo.findAgreementByNumber(agr.getNumber());
            if (agreement != null) {
                String response = "Параметр № Дополнительного соглашения (сделки) Number <"+agr.getNumber()+"> уже существует";
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
        }

        tppProduct = saveProduct(corporateSettlementInstanceDTO);
        Account account = findAccount(corporateSettlementInstanceDTO);
        TppProductRegister tppProductRegister = saveTppProductRegister(corporateSettlementInstanceDTO, tppProduct, account);
        List<TppProductRegister> tppProductRegisters = new ArrayList<>();
        tppProductRegisters.add(tppProductRegister);

        return new ResponseEntity<>(buildOkResponse(corporateSettlementInstanceDTO, tppProductRegisters), HttpStatus.OK);
    }

    private ResponseEntity<String> updateInstance(CorporateSettlementInstanceDTO corporateSettlementInstanceDTO){
        Optional<TppProduct> tppProduct = tppProductRepo.findById(corporateSettlementInstanceDTO.getInstanceId());
        if (tppProduct.isEmpty()){
            String response = "Экземпляр продукта с параметром instanceId <"+
                    corporateSettlementInstanceDTO.getInstanceId()+
                    "> не найден";
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        for(var agr : corporateSettlementInstanceDTO.getAgreements()){
            Agreement agreement = agreementRepo.findAgreementByNumber(agr.getNumber());
            if (agreement != null) {
                String response = "Параметр № Дополнительного соглашения (сделки) Number <"+agr.getNumber()+"> уже существует";
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            } else {
                agr.setProductId(tppProduct.get().getId());
                agreementRepo.save(agr);
            }
        }

        List<TppProductRegister> tppProductRegisters = tppProductRegisterRepo.findTppProductRegisterByProductId(tppProduct.get().getId());

        return new ResponseEntity<>(buildOkResponse(corporateSettlementInstanceDTO, tppProductRegisters), HttpStatus.OK);
    }

    private String buildOkResponse(CorporateSettlementInstanceDTO corporateSettlementInstanceDTO,
                                   List<TppProductRegister> tppProductRegisters){
        JsonArrayBuilder joTppProductRegisters = Json.createArrayBuilder();
        tppProductRegisters.forEach((tppProductRegister) -> joTppProductRegisters.add(tppProductRegister.getId()));

        JsonArrayBuilder joAgreements = Json.createArrayBuilder();
        for(var agreement : corporateSettlementInstanceDTO.getAgreements()){
            if (agreement.getId() != null) {
                joAgreements.add(agreement.getId());
            }
        }

        return Json.createObjectBuilder()
                .add("data", Json.createObjectBuilder()
                                .add("instanceId", String.valueOf(corporateSettlementInstanceDTO.getInstanceId()))
                                .add("registerId", joTppProductRegisters)
                                .add("supplementaryAgreementId", joAgreements))
                .build()
                .toString();
    }

    private TppProduct saveProduct(CorporateSettlementInstanceDTO corporateSettlementInstanceDTO){
        TppProduct tppProduct = new TppProduct();
        tppProduct.setProductCodeId(tppRefProductClassRepo.findByValue(corporateSettlementInstanceDTO.getProductCode()).getInternalId());
        tppProduct.setType(corporateSettlementInstanceDTO.getProductType());
        tppProduct.setNumber(corporateSettlementInstanceDTO.getContractNumber());
        tppProduct.setPriority(corporateSettlementInstanceDTO.getPriority());
        tppProduct.setDateOfConclusion(corporateSettlementInstanceDTO.getContractDate());
        tppProduct.setStartDateTime(corporateSettlementInstanceDTO.getContractDate());
        tppProduct.setThresholdAmount(corporateSettlementInstanceDTO.getThresholdAmount());
        tppProduct.setTaxRate(corporateSettlementInstanceDTO.getTaxPercentageRate());
        tppProductRepo.save(tppProduct);

        return tppProduct;
    }

    private Account findAccount(CorporateSettlementInstanceDTO corporateSettlementInstanceDTO){
        AccountPool accountPool = accountPoolRepo.findAccountPoolByAllReq(
                corporateSettlementInstanceDTO.getBranchCode(),
                corporateSettlementInstanceDTO.getIsoCurrencyCode(),
                corporateSettlementInstanceDTO.getMdmCode(),
                corporateSettlementInstanceDTO.getUrgencyCode(),
                corporateSettlementInstanceDTO.getRegisterType());

        return accountRepo.findFirstByAccountPoolIdOrderById(accountPool.getId());
    }

    private TppProductRegister saveTppProductRegister(CorporateSettlementInstanceDTO corporateSettlementInstanceDTO, TppProduct tppProduct, Account account){
        TppProductRegister tppProductRegister = new TppProductRegister();
        tppProductRegister.setProductId(tppProduct.getId());
        tppProductRegister.setType(corporateSettlementInstanceDTO.getRegisterType());
        tppProductRegister.setAccount(account.getId());
        tppProductRegister.setCurrencyCode(corporateSettlementInstanceDTO.getIsoCurrencyCode());
        tppProductRegister.setAccountNumber(account.getAccountNumber());
        tppProductRegister.setState(String.valueOf(ProductRegisterStateEnum.OPEN));

        tppProductRegisterRepo.save(tppProductRegister);

        return tppProductRegister;
    }
}
