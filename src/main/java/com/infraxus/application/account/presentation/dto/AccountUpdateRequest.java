package com.infraxus.application.account.presentation.dto;

import com.infraxus.application.account.domain.Account;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AccountUpdateRequest {
    private String accountType;
    private String accountName;
    private String accountPwd;
    private String accountToken;

    @Builder
    public AccountUpdateRequest(String accountType, String accountName, String accountPwd, String accountToken) {
        this.accountType = accountType;
        this.accountName = accountName;
        this.accountPwd = accountPwd;
        this.accountToken = accountToken;
    }

    public Account toEntity() {
        return Account.updateBuilder()
                .accountType(this.accountType)
                .accountName(this.accountName)
                .accountPwd(this.accountPwd)
                .accountToken(this.accountToken)
                .build();
    }
}
