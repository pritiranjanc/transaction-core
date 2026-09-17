package com.transaction.core.controller;

import com.transaction.core.dto.response.AccountDTO;
import com.transaction.core.dto.response.TransactionHistory;
import com.transaction.core.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @GetMapping("/{accountId}/transactions")
    public Page<TransactionHistory> getTransactionHistory(
            @PathVariable Long accountId,
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC)
            Pageable pageable) {
        return accountService.getTransactionHistory(accountId, pageable);
    }

    @GetMapping("/{accountId}/balance")
    public AccountDTO getBalance(@PathVariable Long accountId) {
        return accountService.getAccount(accountId);
    }

}
