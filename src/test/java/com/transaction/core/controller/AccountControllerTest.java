package com.transaction.core.controller;

import com.transaction.core.dto.response.AccountDTO;
import com.transaction.core.dto.response.PageDTO;
import com.transaction.core.dto.response.TransactionHistory;
import com.transaction.core.service.AccountService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AccountControllerTest {

    @InjectMocks
    private AccountController accountController;

    @Mock
    private AccountService accountService;

    @Test
    public void testAccountBalance(){
        when(accountService.getAccountBalance(anyLong())).thenReturn(AccountDTO.builder().build());
        AccountDTO account = accountController.getBalance(1L);
        assertNotNull(account);
        verify(accountService,times(1)).getAccountBalance(anyLong());
    }

    @Test
    public void testGetTransactionHistory(){
        when(accountService.getTransactionHistory(anyLong(),any())).thenReturn(PageDTO.<TransactionHistory>builder().build());
        PageDTO<TransactionHistory> transactions = accountController.getTransactionHistory(1L,0,10);
        assertNotNull(transactions);
        verify(accountService,times(1)).getTransactionHistory(anyLong(),any());
    }



}
