package com.transaction.core.validator;

import com.transaction.core.entity.Account;

import java.math.BigDecimal;

public interface AccountValidator {

    String getSupportedType();

    void validate(Account account, BigDecimal amount);

}
