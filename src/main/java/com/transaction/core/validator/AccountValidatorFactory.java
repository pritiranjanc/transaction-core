package com.transaction.core.validator;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class AccountValidatorFactory {

    private final Map<String, AccountValidator> validators;

    public AccountValidatorFactory(List<AccountValidator> validators) {
        this.validators = validators.stream()
                .collect(Collectors.toMap(AccountValidator::getSupportedType, Function.identity()));
    }

    public AccountValidator getValidator(String accountType){
        return validators.get(accountType);
    }

}
