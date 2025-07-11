package com.infraxus.application.account.domain.exception;

import com.infraxus.global.exception.BaseException;
import org.springframework.http.HttpStatus;

public class AccountNotFoundException extends BaseException {
    public AccountNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
