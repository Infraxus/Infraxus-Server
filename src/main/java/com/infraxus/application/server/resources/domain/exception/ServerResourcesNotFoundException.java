package com.infraxus.application.server.resources.domain.exception;

import com.infraxus.global.exception.BaseException;
import org.springframework.http.HttpStatus;

public class ServerResourcesNotFoundException extends BaseException {
    public ServerResourcesNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
