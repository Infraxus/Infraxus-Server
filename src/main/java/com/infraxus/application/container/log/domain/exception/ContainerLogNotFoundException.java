package com.infraxus.application.container.log.domain.exception;

import com.infraxus.global.exception.BaseException;
import org.springframework.http.HttpStatus;

public class ContainerLogNotFoundException extends BaseException {
    public ContainerLogNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
