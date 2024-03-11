package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.example.corporateSettlementInstance.dto.CorporateSettlementInstanceDTO;
import org.example.entities.Agreement;
import org.example.entities.TppProduct;
import org.example.enums.ProductTypeEnum;
import org.example.repos.AgreementRepo;
import org.example.repos.TppProductRegisterRepo;
import org.example.repos.TppProductRepo;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CorporateSettlementInstanceTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    TppProductRepo tppProductRepo;
    @Autowired
    TppProductRegisterRepo tppProductRegisterRepo;
    @Autowired
    AgreementRepo agreementRepo;
    MvcResult result;

    String asJsonString(Object obj) {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @BeforeEach
    void beforeSetup() {
        agreementRepo.deleteAll();
        tppProductRepo.deleteAll();
        tppProductRegisterRepo.deleteAll();
    }
    @AfterEach
    void afterPerform(TestInfo testInfo) throws Exception {
        log.info("test method " +testInfo.getDisplayName() + " got mvc.result.response " + result.getResponse().getContentAsString());
    }

    @Test
    @Order(1)
    void createOkRequestWithNullInstanceID() throws Exception {
        Agreement agreement = new Agreement();
        agreement.setNumber("1");
        agreement.setOpeningDate(Date.valueOf(LocalDate.now()));
        List<Agreement> agreements = new ArrayList<>();
        agreements.add(agreement);

        CorporateSettlementInstanceDTO corporateSettlementInstanceDTO = CorporateSettlementInstanceDTO.builder()
                .productType(ProductTypeEnum.DBDS)
                .productCode("03.012.002")
                .contractNumber("1")
                .contractDate(Date.valueOf(LocalDate.now()))
                .contractId(1)
                .urgencyCode("1")
                .referenceCode(1)
                .branchCode("0022")
                .isoCurrencyCode("800")
                .mdmCode("15")
                .urgencyCode("00")
                .priority(1L)
                .registerType("03.012.002_47533_ComSoLd")
                .agreements(agreements)
                .build();

        result = mockMvc.perform(post("/corporate-settlement-instance/create")
                .content(asJsonString(corporateSettlementInstanceDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        assertEquals(1, tppProductRepo.count());
        assertEquals(1, tppProductRegisterRepo.count());
    }

    @Test
    @Order(2)
    void createOkRequestWithNotNullInstanceID() throws Exception {
        TppProduct tppProduct = new TppProduct();
        tppProductRepo.save(tppProduct);

        Agreement agreement = new Agreement();
        agreement.setNumber("1");
        agreement.setOpeningDate(Date.valueOf(LocalDate.now()));
        List<Agreement> agreements = new ArrayList<>();
        agreements.add(agreement);

        CorporateSettlementInstanceDTO corporateSettlementInstanceDTO = CorporateSettlementInstanceDTO.builder()
                .instanceId(tppProduct.getId())
                .productType(ProductTypeEnum.DBDS)
                .productCode("03.012.002")
                .contractNumber("1")
                .contractDate(Date.valueOf(LocalDate.now()))
                .contractId(1)
                .urgencyCode("1")
                .referenceCode(1)
                .branchCode("0022")
                .isoCurrencyCode("800")
                .mdmCode("15")
                .urgencyCode("00")
                .priority(1L)
                .registerType("03.012.002_47533_ComSoLd")
                .agreements(agreements)
                .build();

        result = mockMvc.perform(post("/corporate-settlement-instance/create")
                        .content(asJsonString(corporateSettlementInstanceDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        assertEquals(1, tppProductRepo.count());
        assertEquals(1, agreementRepo.count());
    }

    @Test
    @Order(3)
    void createBadRequestWithNullRequiredParams() throws Exception {

        CorporateSettlementInstanceDTO corporateSettlementInstanceDTO = CorporateSettlementInstanceDTO.builder()
                .instanceId(null)
                .build();

        result = mockMvc.perform(post("/corporate-settlement-instance/create")
                        .content(asJsonString(corporateSettlementInstanceDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    @Order(4)
    void createBadRequestWithExistingProductAndNullInstanceID() throws Exception {
        TppProduct tppProduct = new TppProduct();
        tppProduct.setNumber("contract_number_tst");
        tppProductRepo.save(tppProduct);

        Agreement agreement = new Agreement();
        agreement.setNumber("1");
        agreement.setOpeningDate(Date.valueOf(LocalDate.now()));
        List<Agreement> agreements = new ArrayList<>();
        agreements.add(agreement);

        CorporateSettlementInstanceDTO corporateSettlementInstanceDTO = CorporateSettlementInstanceDTO.builder()
                .productType(ProductTypeEnum.DBDS)
                .productCode("03.012.002")
                .contractNumber("contract_number_tst")
                .contractDate(Date.valueOf(LocalDate.now()))
                .contractId(1)
                .urgencyCode("1")
                .referenceCode(1)
                .branchCode("0022")
                .isoCurrencyCode("800")
                .mdmCode("15")
                .urgencyCode("00")
                .priority(1L)
                .registerType("03.012.002_47533_ComSoLd")
                .agreements(agreements)
                .build();

        result = mockMvc.perform(post("/corporate-settlement-instance/create")
                        .content(asJsonString(corporateSettlementInstanceDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    @Order(5)
    void createBadRequestWithExistingAgreementAndNullInstanceID() throws Exception {
        Agreement agreement = new Agreement();
        agreement.setNumber("agreement_num_tst");
        agreement.setOpeningDate(Date.valueOf(LocalDate.now()));
        List<Agreement> agreements = new ArrayList<>();
        agreements.add(agreement);
        agreementRepo.save(agreement);

        CorporateSettlementInstanceDTO corporateSettlementInstanceDTO = CorporateSettlementInstanceDTO.builder()
                .productType(ProductTypeEnum.DBDS)
                .productCode("03.012.002")
                .contractNumber("contract_number_tst")
                .contractDate(Date.valueOf(LocalDate.now()))
                .contractId(1)
                .urgencyCode("1")
                .referenceCode(1)
                .branchCode("0022")
                .isoCurrencyCode("800")
                .mdmCode("15")
                .urgencyCode("00")
                .priority(1L)
                .registerType("03.012.002_47533_ComSoLd")
                .agreements(agreements)
                .build();

        result = mockMvc.perform(post("/corporate-settlement-instance/create")
                        .content(asJsonString(corporateSettlementInstanceDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    @Order(6)
    void createBadRequestWithExistingAgreementAndNotNullInstanceID() throws Exception {
        Agreement agreement = new Agreement();
        agreement.setNumber("1");
        agreement.setOpeningDate(Date.valueOf(LocalDate.now()));
        List<Agreement> agreements = new ArrayList<>();
        agreements.add(agreement);

        CorporateSettlementInstanceDTO corporateSettlementInstanceDTO = CorporateSettlementInstanceDTO.builder()
                .instanceId(-1L)
                .productType(ProductTypeEnum.DBDS)
                .productCode("03.012.002")
                .contractNumber("contract_number_tst")
                .contractDate(Date.valueOf(LocalDate.now()))
                .contractId(1)
                .urgencyCode("1")
                .referenceCode(1)
                .branchCode("0022")
                .isoCurrencyCode("800")
                .mdmCode("15")
                .urgencyCode("00")
                .priority(1L)
                .registerType("03.012.002_47533_ComSoLd")
                .agreements(agreements)
                .build();

        result = mockMvc.perform(post("/corporate-settlement-instance/create")
                        .content(asJsonString(corporateSettlementInstanceDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    @Order(7)
    void createNotFoundRequestWithNotExistingProductAndNotNullInstanceID() throws Exception {
        TppProduct tppProduct = new TppProduct();
        tppProductRepo.save(tppProduct);

        Agreement agreement = new Agreement();
        agreement.setNumber("1");
        agreement.setOpeningDate(Date.valueOf(LocalDate.now()));
        List<Agreement> agreements = new ArrayList<>();
        agreements.add(agreement);
        agreementRepo.save(agreement);

        CorporateSettlementInstanceDTO corporateSettlementInstanceDTO = CorporateSettlementInstanceDTO.builder()
                .instanceId(tppProduct.getId())
                .productType(ProductTypeEnum.DBDS)
                .productCode("03.012.002")
                .contractNumber("contract_number_tst")
                .contractDate(Date.valueOf(LocalDate.now()))
                .contractId(1)
                .urgencyCode("1")
                .referenceCode(1)
                .branchCode("0022")
                .isoCurrencyCode("800")
                .mdmCode("15")
                .urgencyCode("00")
                .priority(1L)
                .registerType("03.012.002_47533_ComSoLd")
                .agreements(agreements)
                .build();

        result = mockMvc.perform(post("/corporate-settlement-instance/create")
                        .content(asJsonString(corporateSettlementInstanceDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
    }
}
