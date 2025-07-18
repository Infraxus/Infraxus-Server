package com.infraxus.application.setting.exception;

import com.infraxus.global.exception.BaseException;
import com.infraxus.global.exception.ErrorCode;

public class SettingNotFoundException extends BaseException {
    public SettingNotFoundException() {
        super(ErrorCode.SETTING_NOT_FOUND);
    }

    public SettingNotFoundException(String message) {
        super(message, ErrorCode.SETTING_NOT_FOUND);
    }
}
