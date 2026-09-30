package com.transaction.core.controller;

import com.transaction.core.dto.response.PageDTO;
import com.transaction.core.dto.response.TransactionHistory;
import com.transaction.core.service.AccountService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.transaction.core.utils.TestUtils.getAccountDTO;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AccountController.class)
@ImportAutoConfiguration(exclude = {SecurityAutoConfiguration.class})
public class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AccountService accountService;

    @Test
    public void testAccountBalance() throws Exception {
        when(accountService.getAccountBalance(anyLong())).thenReturn(getAccountDTO());
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/accounts/1/balance"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value("1500.0"));
    }

   @Test
   public void testGetTransactionHistory() throws Exception {
       when(accountService.getTransactionHistory(anyLong(),any())).thenReturn(PageDTO.<TransactionHistory>builder().build());
       mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/accounts/1/transactions"))
               .andExpect(status().isOk());
       verify(accountService,times(1)).getTransactionHistory(anyLong(),any());
   }

}
