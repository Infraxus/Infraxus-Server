package com.infraxus.application.dashboard.exception;

import com.infraxus.global.exception.BaseException;
import com.infraxus.global.exception.ErrorCode;

public class DashboardNotFoundException extends BaseException {
    public DashboardNotFoundException() {
        super(ErrorCode.DASHBOARD_NOT_FOUND);
    }

    public DashboardNotFoundException(String message) {
        super(message, ErrorCode.DASHBOARD_NOT_FOUND);
    }
}
