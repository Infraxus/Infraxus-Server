package com.infraxus.application.server.server.exception;

import com.infraxus.global.exception.BaseException;
import com.infraxus.global.exception.ErrorCode;

public class ServerNotFoundException extends BaseException {
    public ServerNotFoundException() {
        super(ErrorCode.SERVER_NOT_FOUND);
    }

    public ServerNotFoundException(String message) {
        super(message, ErrorCode.SERVER_NOT_FOUND);
    }
}
