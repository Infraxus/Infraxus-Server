package com.infraxus.application.container.container.domain.exception;

import com.infraxus.global.exception.BaseException;
import org.springframework.http.HttpStatus;

public class ContainerNotFoundException extends BaseException {
    public ContainerNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
