package com.infraxus.application.account.domain;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.util.UUID;

@Getter
@Table("account")
public class Account {

    @PrimaryKey
    private UUID accountId;

    private String accountType;

    private String accountName;

    private String accountPwd;

    private String accountToken;

    @Builder(builderMethodName = "createBuilder")
    public Account(UUID accountId, String accountType, String accountName, String accountPwd, String accountToken) {
        this.accountId = accountId;
        this.accountType = accountType;
        this.accountName = accountName;
        this.accountPwd = accountPwd;
        this.accountToken = accountToken;
    }

    @Builder(builderMethodName = "updateBuilder")
    public Account(String accountType, String accountName, String accountPwd, String accountToken) {
        this.accountType = accountType;
        this.accountName = accountName;
        this.accountPwd = accountPwd;
        this.accountToken = accountToken;
    }

}
