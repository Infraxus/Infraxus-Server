package com.infraxus.application.account.service.implementation;

import com.infraxus.application.account.domain.Account;
import com.infraxus.application.account.domain.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountUpdater {

    private final AccountRepository accountRepository;

    public void update(Account updatableAccount, Account account){
        updatableAccount.updateBuilder()
                .accountType(account.getAccountType())
                .accountName(account.getAccountName())
                .accountPwd(account.getAccountPwd())
                .accountToken(account.getAccountToken())
                .build();

        accountRepository.save(updatableAccount);
    }

}
