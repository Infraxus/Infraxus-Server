package com.infraxus.application.alram.alert.domain.exception;

import com.infraxus.global.exception.BaseException;
import org.springframework.http.HttpStatus;

public class AlertNotFoundException extends BaseException {
    public AlertNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
