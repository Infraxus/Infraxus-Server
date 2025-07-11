package com.infraxus.application.server.version.domain.exception;

import com.infraxus.global.exception.BaseException;
import org.springframework.http.HttpStatus;

public class ServerVersionNotFoundException extends BaseException {
    public ServerVersionNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
