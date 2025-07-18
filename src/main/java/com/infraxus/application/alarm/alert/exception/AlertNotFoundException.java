package com.infraxus.application.alarm.alert.exception;

import com.infraxus.global.exception.BaseException;
import com.infraxus.global.exception.ErrorCode;

public class AlertNotFoundException extends BaseException {
    public AlertNotFoundException() {
        super(ErrorCode.ALERT_NOT_FOUND);
    }

    public AlertNotFoundException(String message) {
        super(message, ErrorCode.ALERT_NOT_FOUND);
    }
}
