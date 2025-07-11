package com.infraxus.application.account.service.implementation;

import com.infraxus.application.account.domain.Account;
import com.infraxus.application.account.domain.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountCreator {

    private final AccountRepository accountRepository;

    public void save(Account account){
        accountRepository.save(account);
    }

}
