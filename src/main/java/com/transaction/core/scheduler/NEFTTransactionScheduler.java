package com.transaction.core.scheduler;

import com.transaction.core.constants.TransactionStatus;
import com.transaction.core.constants.TransferType;
import com.transaction.core.repository.TransactionRepository;
import com.transaction.core.service.transfer.TransferServiceFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
@SuppressWarnings(value = "unused")
public class NEFTTransactionScheduler {

    private final TransactionRepository transactionRepository;
    private final TransferServiceFactory transferServiceFactory;

    @Value("${app.scheduler.neft.batch-size:10000}")
    private  int batchSize;

    @Scheduled(fixedDelayString = "${app.scheduler.neft.scheduled-delay:10000}")
    public void process(){
        log.info("Transaction Batch Processing started!! {}",batchSize);
        List<Long> transactionIds = transactionRepository.findIdByStatusAndTransferTypeOrderById(TransactionStatus.PENDING,
                TransferType.NEFT, PageRequest.of(0,batchSize));
        log.info("Transaction Batch Processing size!! {}",transactionIds.size());
        transactionIds.forEach(txn-> {
                    try {
                        transferServiceFactory.getTransferService(TransferType.NEFT.name()).processPendingTransfer(txn);
                        log.info("Transaction Processing Completed {}",txn);
                    }catch(Exception ex){
                        log.error("Transaction Processing Failed {}",txn);
                    }
                }
        );
    }
}
