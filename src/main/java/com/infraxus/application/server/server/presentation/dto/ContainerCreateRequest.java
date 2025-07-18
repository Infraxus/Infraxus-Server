package com.infraxus.application.server.server.presentation.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class ContainerCreateRequest {
    private String containerName;
    private String containerState;
    private String containerDescription;
    private Integer buildCount;
    private List<String> envVars;
    private List<String> volumes;
    private String githubLink;
    private String dockerfilePath;
    private String externalPort;
    private String internalPort;
    private String filePath;
    private String image;
    private String language;
    private String framework;
    private String database;
    private String healthCheckPath;
    private Integer healthCheckCycle;

    @Builder
    public ContainerCreateRequest(
            String containerName,
            Integer buildCount,
            String dockerfilePath,
            String containerDescription,
            String containerState,
            String externalPort,
            String internalPort,
            String githubLink,
            String filePath,
            String image,
            String language,
            String framework,
            String database,
            List<String> envVars,
            List<String> volumes,
            String healthCheckPath,
            Integer healthCheckCycle
    ) {
        this.containerName = containerName;
        this.buildCount = buildCount;
        this.dockerfilePath = dockerfilePath;
        this.containerDescription = containerDescription;
        this.containerState = containerState;
        this.externalPort = externalPort;
        this.internalPort = internalPort;
        this.githubLink = githubLink;
        this.filePath = filePath;
        this.image = image;
        this.database = database;
        this.language = language;
        this.framework = framework;
        this.healthCheckPath = healthCheckPath;
        this.healthCheckCycle = healthCheckCycle;
        this.envVars = envVars;
        this.volumes = volumes;
    }
}
