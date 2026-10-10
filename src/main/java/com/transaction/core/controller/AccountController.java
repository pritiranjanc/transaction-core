package com.transaction.core.controller;

import com.transaction.core.dto.response.AccountDTO;
import com.transaction.core.dto.response.PageDTO;
import com.transaction.core.dto.response.LedgerEntryDTO;
import com.transaction.core.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
@Tag(name = "Account Summary", description = "Account Summary APIs")
public class AccountController {

    private final AccountService accountService;

    @Operation(description = "Get transaction history for an account",summary = "Get Transaction history")
    @GetMapping("/{accountId}/transactions")
    public PageDTO<LedgerEntryDTO> getTransactionHistory(
            @PathVariable Long accountId,
            @RequestParam(required = false,name = "page",defaultValue = "0") Integer page,
            @RequestParam(required = false,name = "page-size", defaultValue = "10") Integer pageSize) {
        return accountService.getTransactionHistory(accountId,
                PageRequest.of(page,pageSize, Sort.by(Sort.Direction.DESC, "createdAt")));
    }

    @Operation(description = "Get account balance for an account",summary = "Get Account balance")
    @GetMapping("/{accountId}/balance")
    public AccountDTO getBalance(@PathVariable Long accountId) {
        return accountService.getAccountBalance(accountId);
    }

}
