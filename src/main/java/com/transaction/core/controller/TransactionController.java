package com.transaction.core.controller;


import com.transaction.core.constants.TransferType;
import com.transaction.core.dto.request.Deposit;
import com.transaction.core.dto.request.Transfer;
import com.transaction.core.dto.request.Withdraw;
import com.transaction.core.dto.response.TransactionDTO;
import com.transaction.core.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/transactions")
@RequiredArgsConstructor
@Tag(name = "Transactions", description = "Transaction management APIs")
@SuppressWarnings(value = "unused")
public class TransactionController {

    private final TransactionService transactionService;

    @Operation(description = "To deposit the amount for an account",summary = "To deposit amount")
    @PostMapping("/deposit")
    public TransactionDTO processDeposit(@Valid @RequestBody Deposit request) {
        return transactionService.processDeposit(request.accountId(), request.amount());
    }

    @Operation(description = "To withdraw the amount for an account",summary = "To withdraw amount")
    @PostMapping("/withdraw")
    public TransactionDTO processWithdrawal(@Valid @RequestBody Withdraw request) {
        return transactionService.processWithdrawal(request.accountId(), request.amount());
    }

    @Operation(description = "To transfer the amount from one account to another",summary = "To transfer amount")
    @PostMapping("/{type}/transfer")
    public TransactionDTO processTransfer(
            @Parameter(description = "Supported transfer types", in = ParameterIn.PATH)
            @PathVariable TransferType type, @Valid @RequestBody Transfer request) {
        return transactionService.processTransfer(request.fromAccountId(), request.toAccountId(), request.amount(),type);
    }

}
