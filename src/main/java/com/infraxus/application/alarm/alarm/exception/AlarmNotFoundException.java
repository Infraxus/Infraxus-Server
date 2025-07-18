package com.infraxus.application.alarm.alarm.exception;

import com.infraxus.global.exception.BaseException;
import com.infraxus.global.exception.ErrorCode;

public class AlarmNotFoundException extends BaseException {
    public AlarmNotFoundException() {
        super(ErrorCode.ALARM_NOT_FOUND);
    }

    public AlarmNotFoundException(String message) {
        super(message, ErrorCode.ALARM_NOT_FOUND);
    }
}
