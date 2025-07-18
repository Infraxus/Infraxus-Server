package com.infraxus.application.server.server.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CiCdGenerateRequest {
    private String projectName;
    private String serviceName;
    private String languageFramework;
    private String database;
    private String messagingSystem;
    private String exposedPort;
}
