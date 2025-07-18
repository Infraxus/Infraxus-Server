package com.infraxus.application.container.log.exception;

import com.infraxus.global.exception.BaseException;
import com.infraxus.global.exception.ErrorCode;

public class ContainerLogNotFoundException extends BaseException {
    public ContainerLogNotFoundException() {
        super(ErrorCode.CONTAINER_LOG_NOT_FOUND);
    }

    public ContainerLogNotFoundException(String message) {
        super(message, ErrorCode.CONTAINER_LOG_NOT_FOUND);
    }
}
