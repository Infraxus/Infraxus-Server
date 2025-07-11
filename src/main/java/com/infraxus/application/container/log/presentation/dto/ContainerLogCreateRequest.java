package com.infraxus.application.container.log.presentation.dto;

import com.infraxus.application.container.log.domain.ContainerLog;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Getter
@NoArgsConstructor
public class ContainerLogCreateRequest {
    private UUID serverId;
    private String logType;
    private String logLevel;
    private String logTitle;
    private String logContent;
    private String stackTrace;

    @Builder
    public ContainerLogCreateRequest(UUID serverId, String logType, String logLevel, String logTitle, String logContent, String stackTrace) {
        this.serverId = serverId;
        this.logType = logType;
        this.logLevel = logLevel;
        this.logTitle = logTitle;
        this.logContent = logContent;
        this.stackTrace = stackTrace;
    }

    public ContainerLog toEntity() {
        return ContainerLog.builder()
                .containerId(UUID.randomUUID())
                .serverId(this.serverId)
                .logType(this.logType)
                .logLevel(this.logLevel)
                .logTitle(this.logTitle)
                .logContent(this.logContent)
                .stackTrace(this.stackTrace)
                .createAt(new Date())
                .build();
    }
}
