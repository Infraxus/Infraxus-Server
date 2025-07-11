package com.infraxus.application.account.presentation.dto;

import com.infraxus.application.account.domain.Account;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
public class AccountCreateRequest {
    private String accountType;
    private String accountName;
    private String accountPwd;
    private String accountToken;

    @Builder
    public AccountCreateRequest(String accountType, String accountName, String accountPwd, String accountToken) {
        this.accountType = accountType;
        this.accountName = accountName;
        this.accountPwd = accountPwd;
        this.accountToken = accountToken;
    }

    public Account toEntity() {
        return Account.createBuilder()
                .accountId(UUID.randomUUID())
                .accountType(this.accountType)
                .accountName(this.accountName)
                .accountPwd(this.accountPwd)
                .accountToken(this.accountToken)
                .build();
    }
}
