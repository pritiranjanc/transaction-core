package com.transaction.core.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testAccountBalance() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/accounts/1/balance"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value("1500.0"));
    }

   @Test
   public void testGetTransactionHistory() throws Exception {
       mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/accounts/1/transactions"))
               .andExpect(status().isOk());
   }

}
