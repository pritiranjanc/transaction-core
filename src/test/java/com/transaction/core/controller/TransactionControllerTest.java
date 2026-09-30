package com.transaction.core.controller;


import com.transaction.core.constants.TransactionStatus;
import com.transaction.core.constants.TransactionType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testProcessWithdrawal() throws Exception {
        String requestBody = """
        {
            "accountId": "1",
            "amount": "100"
        }
        """;
        mockMvc.perform(post("/api/v1/transactions/withdraw").contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.transactionType").value(TransactionType.WITHDRAWAL.name()))
                .andExpect(jsonPath("$.status").value(TransactionStatus.COMPLETED.name()))
                .andExpect(jsonPath("$.amount").value("100"));
    }

    @Test
    void testProcessDeposit() throws Exception {
        String requestBody = """
        {
            "accountId": "1",
            "amount": "100"
        }
        """;
        mockMvc.perform(post("/api/v1/transactions/deposit").contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.transactionType").value(TransactionType.DEPOSIT.name()))
                .andExpect(jsonPath("$.status").value(TransactionStatus.COMPLETED.name()))
                .andExpect(jsonPath("$.amount").value("100"));

    }

    @Test
    void testProcessTransfer() throws Exception {
        String requestBody = """
        {
            "fromAccountId": "1",
            "toAccountId": "2",
            "amount": "100"
        }
        """;
        mockMvc.perform(post("/api/v1/transactions/transfer").contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.transactionType").value(TransactionType.TRANSFER.name()))
                .andExpect(jsonPath("$.status").value(TransactionStatus.COMPLETED.name()))
                .andExpect(jsonPath("$.amount").value("100"));

    }

}
