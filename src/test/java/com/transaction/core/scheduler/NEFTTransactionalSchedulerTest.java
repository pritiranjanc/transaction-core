package com.transaction.core.scheduler;

import com.transaction.core.repository.TransactionRepository;
import com.transaction.core.service.transfer.NEFTTransferService;
import com.transaction.core.service.transfer.TransferServiceFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.stereotype.Component;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class NEFTTransactionalSchedulerTest {

    @InjectMocks
    private NEFTTransactionScheduler neftTransactionScheduler;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private TransferServiceFactory transferServiceFactory;

    @Mock
    private NEFTTransferService neftTransferService;

    @Test
    void testProcess(){
        ReflectionTestUtils.setField(neftTransactionScheduler, "batchSize", 100);
        when(transactionRepository.findIdByStatusAndTransferTypeOrderById(any(),any(),any())).thenReturn(List.of(1L));
        when(transferServiceFactory.getTransferService(any())).thenReturn(neftTransferService);
        doNothing().when(neftTransferService).processPendingTransfer(any());
        neftTransactionScheduler.process();
        verify(neftTransferService,times(1)).processPendingTransfer(any());
    }


}
