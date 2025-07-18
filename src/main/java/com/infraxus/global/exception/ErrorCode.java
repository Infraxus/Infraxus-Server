package com.infraxus.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // Common
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "C001", "Invalid Input Value"),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "C002", "Method Not Allowed"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "C003", "Internal Server Error"),

    // Account
    ACCOUNT_NOT_FOUND(HttpStatus.NOT_FOUND, "A001", "Account not found"),

    // Dashboard
    DASHBOARD_NOT_FOUND(HttpStatus.NOT_FOUND, "D001", "Dashboard not found"),

    // Setting
    SETTING_NOT_FOUND(HttpStatus.NOT_FOUND, "S001", "Setting not found"),

    // Container
    CONTAINER_NOT_FOUND(HttpStatus.NOT_FOUND, "CN001", "Container not found"),
    CONTAINER_LOG_NOT_FOUND(HttpStatus.NOT_FOUND, "CN002", "Container log not found"),

    // Server
    SERVER_NOT_FOUND(HttpStatus.NOT_FOUND, "SV001", "Server not found"),
    SERVER_VERSION_NOT_FOUND(HttpStatus.NOT_FOUND, "SV002", "Server version not found"),
    SERVER_RESOURCES_NOT_FOUND(HttpStatus.NOT_FOUND, "SV003", "Server resources not found"),
    SERVER_DISTRIBUTION_NOT_FOUND(HttpStatus.NOT_FOUND, "SV004", "Server distribution not found"),

    // Alarm
    ALARM_NOT_FOUND(HttpStatus.NOT_FOUND, "AL001", "Alarm not found"),
    ALERT_NOT_FOUND(HttpStatus.NOT_FOUND, "AL002", "Alert not found");


    private final HttpStatus status;
    private final String code;
    private final String message;
}
