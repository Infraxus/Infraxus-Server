package com.infraxus.application.server.resources.exception;

import com.infraxus.global.exception.BaseException;
import com.infraxus.global.exception.ErrorCode;

public class ServerResourcesNotFoundException extends BaseException {
    public ServerResourcesNotFoundException() {
        super(ErrorCode.SERVER_RESOURCES_NOT_FOUND);
    }

    public ServerResourcesNotFoundException(String message) {
        super(message, ErrorCode.SERVER_RESOURCES_NOT_FOUND);
    }
}
