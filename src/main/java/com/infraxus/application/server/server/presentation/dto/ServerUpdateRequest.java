package com.infraxus.application.server.server.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServerUpdateRequest {
    private String serverName;
    private String serverType;
    private String jenkinsfilePath;
    private String dockerComposeFilePath;

    private String serverState;
    private List<String> skillStack;
    private Boolean rollBack;
    private java.time.LocalDateTime rebuildTime;

    // ServerResource
    private Integer cpuResource;
    private Integer memoryResource;
    private Integer storageResource;
    private Integer gpuResource;

    // Deployment fields (for orchestration updates)
    private String imageName;
    private Integer targetReplicas;
    private List<String> portBindings;
    private List<String> envVars;
    private List<String> volumes;
    private String metricsPort;
    private String network;
}
