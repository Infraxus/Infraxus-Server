package com.infraxus.application.dashboard.domain.exception;

import com.infraxus.global.exception.BaseException;
import org.springframework.http.HttpStatus;

public class DashboardNotFoundException extends BaseException {
    public DashboardNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
