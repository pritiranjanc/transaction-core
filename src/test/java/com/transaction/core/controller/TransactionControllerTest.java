package com.transaction.core.controller;


import com.transaction.core.dto.request.Deposit;
import com.transaction.core.dto.request.Transfer;
import com.transaction.core.dto.request.Withdraw;
import com.transaction.core.dto.response.AccountDTO;
import com.transaction.core.dto.response.TransactionDTO;
import com.transaction.core.service.AccountService;
import com.transaction.core.service.TransactionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TransactionControllerTest {

    @InjectMocks
    private TransactionController transactionController;

    @Mock
    private TransactionService transactionService;

    @Test
    public void testProcessWithdrawal(){
        when(transactionService.processWithdrawal(anyLong(), any())).thenReturn(TransactionDTO.builder().build());
        TransactionDTO transaction = transactionController.processWithdrawal(new Withdraw(1L,BigDecimal.TEN));
        assertNotNull(transaction);
        verify(transactionService,times(1)).processWithdrawal(anyLong(),any());
    }

    @Test
    public void testProcessDeposit(){
        when(transactionService.processDeposit(anyLong(), any())).thenReturn(TransactionDTO.builder().build());
        TransactionDTO transaction = transactionController.processDeposit(new Deposit(1L,BigDecimal.TEN));
        assertNotNull(transaction);
        verify(transactionService,times(1)).processDeposit(anyLong(),any());
    }

    @Test
    public void testProcessTransfer(){
        when(transactionService.processTransfer(anyLong(), anyLong(),any())).thenReturn(TransactionDTO.builder().build());
        TransactionDTO transaction = transactionController.processTransfer(new Transfer(1L,2L,BigDecimal.TEN));
        assertNotNull(transaction);
        verify(transactionService,times(1)).processTransfer(anyLong(),anyLong(),any());
    }

}
