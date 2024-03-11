package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.example.corporateSettlementAccount.dto.CorporateSettlementAccountDTO;
import org.example.entities.TppProductRegister;
import org.example.repos.TppProductRegisterRepo;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CorporateSettlementAccountTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    TppProductRegisterRepo tppProductRegisterRepo;
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
        tppProductRegisterRepo.deleteAll();
    }
    @AfterEach
    void afterPerform(TestInfo testInfo) throws Exception {
        log.info("test method " +testInfo.getDisplayName() + " got mvc.result.response " + result.getResponse().getContentAsString());
    }

    @Test
    @Order(1)
    void createOkRequest() throws Exception {

        CorporateSettlementAccountDTO corporateSettlementAccountDTO = CorporateSettlementAccountDTO.builder()
                .instanceId(3L)
                .branchCode("0022")
                .currencyCode("800")
                .mdmCode("15")
                .priorityCode("00")
                .registryTypeCode("03.012.002_47533_ComSoLd")
                .build();

        result = mockMvc.perform(post("/corporate-settlement-account/create")
                .content(asJsonString(corporateSettlementAccountDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        assertEquals(1, tppProductRegisterRepo.count());
    }

    @Test
    @Order(2)
    void createBadRequestWithNullRequiredParams() throws Exception {

        CorporateSettlementAccountDTO corporateSettlementAccountDTO = CorporateSettlementAccountDTO.builder()
                .instanceId(null)
                .build();

        result = mockMvc.perform(post("/corporate-settlement-account/create")
                .content(asJsonString(corporateSettlementAccountDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    @Order(3)
    void createBadRequestWithExistingProductRegister() throws Exception {
        TppProductRegister tppProductRegister = new TppProductRegister();
        tppProductRegister.setProductId(30L);
        tppProductRegister.setType("03.012.002_47533_ComSoLd");
        tppProductRegisterRepo.save(tppProductRegister);

        CorporateSettlementAccountDTO corporateSettlementAccountDTO = CorporateSettlementAccountDTO.builder()
                .instanceId(30L)
                .registryTypeCode("03.012.002_47533_ComSoLd")
                .build();

        result = mockMvc.perform(post("/corporate-settlement-account/create")
                .content(asJsonString(corporateSettlementAccountDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    @Order(4)
    void createNotFoundRequestWithNotExistingProductRegisterType() throws Exception {

        CorporateSettlementAccountDTO corporateSettlementAccountDTO = CorporateSettlementAccountDTO.builder()
                .instanceId(3L)
                .registryTypeCode("not_existing_register_type_code")
                .build();

        result = mockMvc.perform(post("/corporate-settlement-account/create")
                .content(asJsonString(corporateSettlementAccountDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();
    }
}
