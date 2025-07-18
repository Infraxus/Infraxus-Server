package com.infraxus.application.container.container.exception;

import com.infraxus.global.exception.BaseException;
import com.infraxus.global.exception.ErrorCode;

public class ContainerNotFoundException extends BaseException {
    public ContainerNotFoundException() {
        super(ErrorCode.CONTAINER_NOT_FOUND);
    }

    public ContainerNotFoundException(String message) {
        super(message, ErrorCode.CONTAINER_NOT_FOUND);
    }
}
