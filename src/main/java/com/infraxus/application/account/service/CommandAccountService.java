package com.infraxus.application.account.service;

import com.infraxus.application.account.domain.Account;
import com.infraxus.application.account.presentation.dto.AccountCreateRequest;
import com.infraxus.application.account.presentation.dto.AccountUpdateRequest;
import com.infraxus.application.account.service.implementation.AccountCreator;
import com.infraxus.application.account.service.implementation.AccountDeleter;
import com.infraxus.application.account.service.implementation.AccountReader;
import com.infraxus.application.account.service.implementation.AccountUpdater;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CommandAccountService {

    private final AccountCreator accountCreator;
    private final AccountUpdater accountUpdater;
    private final AccountDeleter accountDeleter;
    private final AccountReader accountReader;
    private final QueryAccountService queryAccountService;

    public void createAccount(AccountCreateRequest request){
        accountCreator.save(request.toEntity());
    }

    public void updateAccount(UUID accountId, AccountUpdateRequest request){
        Account account = queryAccountService.getAccountById(accountId);
        accountUpdater.update(account, request.toEntity());
    }

    public void deleteAccount(UUID accountId){
        Account account = queryAccountService.getAccountById(accountId);
        accountDeleter.delete(account);
    }

}
