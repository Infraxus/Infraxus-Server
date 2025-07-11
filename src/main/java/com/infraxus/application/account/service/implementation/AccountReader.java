package com.infraxus.application.account.service.implementation;

import com.infraxus.application.account.domain.Account;
import com.infraxus.application.account.domain.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccountReader {

    private final AccountRepository accountRepository;

    public List<Account> findAll(){
        return accountRepository.findAll();
    }

    public Account findById(UUID id){
        return accountRepository.findById(id)
                .orElse(null);
    }

}
