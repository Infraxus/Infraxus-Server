package com.infraxus.application.account.service;

import com.infraxus.application.account.domain.Account;
import com.infraxus.application.account.domain.exception.AccountNotFoundException;
import com.infraxus.application.account.service.implementation.AccountReader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class QueryAccountService {

    private final AccountReader accountReader;

    public List<Account> findAll(){
        return accountReader.findAll();
    }

    public Account findById(UUID id){
        return accountReader.findById(id);
    }

    public Account getAccountById(UUID accountId) {
        Account account = accountReader.findById(accountId);
        if (account == null) {
            throw new AccountNotFoundException("Account with ID " + accountId + " not found.");
        }
        return account;
    }
}

