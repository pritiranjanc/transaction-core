package com.transaction.core.validator;

import com.transaction.core.constants.AccountType;
import com.transaction.core.entity.Account;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@Slf4j
public class SavingsAccountValidator implements AccountValidator{

    @Override
    public String getSupportedType() {
        return AccountType.SAVINGS.name();
    }

    @Override
    public void validate(Account account, BigDecimal amount) {
        log.info("Savings Validation Successful");
    }
}
