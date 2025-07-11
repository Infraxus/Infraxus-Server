package com.infraxus.application.server.distribution.domain.exception;

import com.infraxus.global.exception.BaseException;
import org.springframework.http.HttpStatus;

public class ServerDistributionNotFoundException extends BaseException {
    public ServerDistributionNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
