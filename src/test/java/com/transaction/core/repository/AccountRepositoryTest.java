package com.transaction.core.repository;


import com.transaction.core.constants.AccountStatus;
import com.transaction.core.entity.Account;
import com.transaction.core.utils.TestUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class AccountRepositoryTest {

    @Autowired
    AccountRepository accountRepository;

    @Test
    void findByIdForUpdate() {
        Account account = TestUtils.getAccount();
        account.setId(null);
        accountRepository.save(account);
        Account result = accountRepository.findByIdForUpdate(1L).orElse(null);
        assert result != null;
        Assertions.assertEquals(AccountStatus.ACTIVE,result.getStatus());
    }
}
