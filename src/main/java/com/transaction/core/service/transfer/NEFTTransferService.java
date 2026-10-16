package com.transaction.core.service.transfer;

import com.transaction.core.constants.TransactionStatus;
import com.transaction.core.constants.TransactionType;
import com.transaction.core.constants.TransferType;
import com.transaction.core.entity.Account;
import com.transaction.core.entity.Transaction;
import com.transaction.core.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

import static com.transaction.core.dto.response.TransactionDTO.createTransaction;

@Service
@RequiredArgsConstructor
public class NEFTTransferService implements TransferService{

    private final TransactionRepository transactionRepository;

    @Override
    public Transaction processTransfer(Account source, Account destination, BigDecimal amount) {
        Transaction transaction = createTransaction(TransactionType.TRANSFER, amount,TransactionStatus.PENDING,source,destination);
        transactionRepository.save(transaction);
        return transaction;
    }

    @Override
    public String supportedTransferType() {
        return TransferType.NEFT.name();
    }

}
