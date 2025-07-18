package com.infraxus.application.server.server.presentation.dto;

import com.infraxus.application.server.server.domain.Server;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServerDetailsResponse {
    // Server fields
    private UUID serverId;
    private String serverName;
    private String serverType;
    private String jenkinsfilePath;
    private String dockerComposeFilePath;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Deployment fields
    private String deploymentName;
    private String imageName;
    private int targetReplicas;
    private List<String> portBindings;
    private List<String> envVars;
    private List<String> volumes;
    private String metricsPort;
    private String network;

    // ServerResources fields
    private Integer cpuResources;
    private Integer memoryResources;
    private Integer diskResources;

    public static ServerDetailsResponse fromEntities(Server server, com.infraxus.application.server.resources.domain.ServerResources serverResources) {
        return ServerDetailsResponse.builder()
                .serverId(server.getServerId())
                .serverName(server.getServerName())
                .serverType(server.getServerType())
                .jenkinsfilePath(server.getJenkinsfilePath())
                .dockerComposeFilePath(server.getDockerComposeFilePath())
                .createdAt(server.getCreatedAt())
                .updatedAt(server.getUpdatedAt())
                .cpuResources(serverResources.getCpuResources())
                .memoryResources(serverResources.getMemoryResources())
                .diskResources(serverResources.getDiskResources())
                .build();
    }
}
