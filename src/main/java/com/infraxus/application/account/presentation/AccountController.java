package com.infraxus.application.account.presentation;

import com.infraxus.application.account.domain.Account;
import com.infraxus.application.account.presentation.dto.AccountCreateRequest;
import com.infraxus.application.account.presentation.dto.AccountUpdateRequest;
import com.infraxus.application.account.service.CommandAccountService;
import com.infraxus.application.account.service.QueryAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final CommandAccountService commandAccountService;
    private final QueryAccountService queryAccountService;

    @GetMapping
    public List<Account> getAccounts(){
        return queryAccountService.findAll();
    }

    @PostMapping
    public ResponseEntity<Void> createAccount(@RequestBody AccountCreateRequest request) {
        commandAccountService.createAccount(request);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{accountId}")
    public ResponseEntity<Void> updateAccount(@PathVariable UUID accountId, @RequestBody AccountUpdateRequest request) {
        commandAccountService.updateAccount(accountId, request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{accountId}")
    public ResponseEntity<Void> deleteAccount(@PathVariable UUID accountId) {
        commandAccountService.deleteAccount(accountId);
        return ResponseEntity.ok().build();
    }
}
