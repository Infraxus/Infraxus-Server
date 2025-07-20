package com.infraxus.application.server.server.presentation.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class ContainerCreateRequest {
    private String containerName;
    private String containerDescription;
    private List<String> envVars;
    private String githubLink;
    private String image;
    private String externalPort;
    private String internalPort;
    private String language;
    private String framework;
    private String database;
    private String healthCheckPath;
    private Integer healthCheckCycle;

    @Builder
    public ContainerCreateRequest(
            String containerName,
            String containerDescription,
            String externalPort,
            String internalPort,
            String githubLink,
            String image,
            String language,
            String framework,
            String database,
            List<String> envVars,
            String healthCheckPath,
            Integer healthCheckCycle
    ) {
        this.containerName = containerName;
        this.containerDescription = containerDescription;
        this.externalPort = externalPort;
        this.internalPort = internalPort;
        this.githubLink = githubLink;
        this.image = image;
        this.database = database;
        this.language = language;
        this.framework = framework;
        this.healthCheckPath = healthCheckPath;
        this.healthCheckCycle = healthCheckCycle;
        this.envVars = envVars;
    }
}
