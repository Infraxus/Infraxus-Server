package com.infraxus.application.alram.alram.domain.exception;

import com.infraxus.global.exception.BaseException;
import org.springframework.http.HttpStatus;

public class AlarmNotFoundException extends BaseException {
    public AlarmNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
