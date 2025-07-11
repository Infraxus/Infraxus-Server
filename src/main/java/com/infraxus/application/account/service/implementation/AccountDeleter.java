package com.infraxus.application.account.service.implementation;

import com.infraxus.application.account.domain.Account;
import com.infraxus.application.account.domain.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountDeleter {

    private final AccountRepository accountRepository;

    public void delete(Account account){
        accountRepository.delete(account);
    }

}
