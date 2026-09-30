package com.transaction.core.controller;


import com.transaction.core.constants.TransactionStatus;
import com.transaction.core.constants.TransactionType;
import com.transaction.core.dto.request.Deposit;
import com.transaction.core.dto.request.Transfer;
import com.transaction.core.dto.request.Withdraw;
import com.transaction.core.dto.response.TransactionDTO;
import com.transaction.core.entity.Account;
import com.transaction.core.repository.AccountRepository;
import com.transaction.core.service.TransactionService;
import com.transaction.core.utils.TestUtils;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private Account account;

    @Autowired
    private AccountRepository accountRepository;

    @BeforeAll
    void testSetUp(){
        account = TestUtils.getAccount();
        account.setId(null);
        accountRepository.save(account);
    }

    @Test
    void testProcessWithdrawal() throws Exception {
        String requestBody = """
        {
            "accountId": "1",
            "amount": "100"
        }
        """;
        mockMvc.perform(
                        post("/api/v1/transactions/withdraw")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                )
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
        mockMvc.perform(
                        post("/api/v1/transactions/deposit")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.transactionType").value(TransactionType.DEPOSIT.name()))
                .andExpect(jsonPath("$.status").value(TransactionStatus.COMPLETED.name()))
                .andExpect(jsonPath("$.amount").value("100"));

    }

    @Test
    void testProcessTransfer() throws Exception {
        Account toAccount = TestUtils.getAccount();
        toAccount.setId(null);
        toAccount.setAccountNumber("AC0002");
        toAccount = accountRepository.save(toAccount);

        String requestBody = """
        {
            "fromAccountId": "1",
            "toAccountId": "2",
            "amount": "100"
        }
        """;
        mockMvc.perform(
                        post("/api/v1/transactions/transfer")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.transactionType").value(TransactionType.TRANSFER.name()))
                .andExpect(jsonPath("$.status").value(TransactionStatus.COMPLETED.name()))
                .andExpect(jsonPath("$.amount").value("100"));

        toAccount = accountRepository.findById(toAccount.getId()).orElse(null);
        assertEquals(new BigDecimal("1600.00"),toAccount.getBalance());
    }

}
