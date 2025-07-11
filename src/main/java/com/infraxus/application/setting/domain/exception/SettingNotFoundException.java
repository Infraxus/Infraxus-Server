package com.infraxus.application.setting.domain.exception;

import com.infraxus.global.exception.BaseException;
import org.springframework.http.HttpStatus;

public class SettingNotFoundException extends BaseException {
    public SettingNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
