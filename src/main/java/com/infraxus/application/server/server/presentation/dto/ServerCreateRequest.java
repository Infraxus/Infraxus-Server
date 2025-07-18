package com.infraxus.application.server.server.presentation.dto;

import com.infraxus.application.server.resources.presentation.dto.ServerResourcesCreateRequest;
import com.infraxus.application.server.server.domain.value.ArchitectureType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServerCreateRequest {
    private String serverName;
    private ArchitectureType architectureType;
    private List<String> skillStack;
    private Boolean rollBack;
    private java.time.LocalDateTime rebuildTime;
    private String dockerComposeFilePath;
    private String jenkinsfilePath;

    private int targetReplicas;
    private String metricsPort;

    private ServerResourcesCreateRequest serverResources;
    private List<ContainerCreateRequest> serverContainers;
}
