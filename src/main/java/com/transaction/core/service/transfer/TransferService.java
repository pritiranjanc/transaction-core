package com.transaction.core.service.transfer;

import com.transaction.core.entity.Account;
import com.transaction.core.entity.Transaction;

import java.math.BigDecimal;

public interface TransferService {

    Transaction processTransfer(Account source, Account destination, BigDecimal amount);

    String supportedTransferType();

}
