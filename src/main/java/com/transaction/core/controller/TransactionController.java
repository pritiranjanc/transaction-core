package com.transaction.core.controller;


import com.transaction.core.dto.request.Deposit;
import com.transaction.core.dto.request.Transfer;
import com.transaction.core.dto.request.Withdraw;
import com.transaction.core.dto.response.TransactionDTO;
import com.transaction.core.service.TransactionService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/transactions")
@RequiredArgsConstructor
@Tag(name = "Transactions", description = "Transaction management APIs")
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping("/deposit")
    public TransactionDTO deposit(@Valid @RequestBody Deposit request) {
        return transactionService.processDeposit(request.accountId(), request.amount());
    }

    @PostMapping("/withdraw")
    public TransactionDTO withdraw(@Valid @RequestBody Withdraw request) {
        return transactionService.processWithdrawal(request.accountId(), request.amount());
    }

    @PostMapping("/transfer")
    public TransactionDTO transfer(@Valid @RequestBody Transfer request) {
        return transactionService.processTransfer(request.fromAccountId(), request.toAccountId(), request.amount());
    }

}
