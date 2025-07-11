package com.infraxus.application.server.server.domain.exception;

import com.infraxus.global.exception.BaseException;
import org.springframework.http.HttpStatus;

public class ServerNotFoundException extends BaseException {
    public ServerNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
