package com.infraxus.application.server.resources.presentation.dto;

import com.infraxus.application.server.resources.domain.ServerResources;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
public class ServerResourcesCreateRequest {
    private Integer cpuResources;
    private Integer memoryResources;
    private Integer diskResources;

    @Builder
    public ServerResourcesCreateRequest(Integer cpuResources, Integer memoryResources, Integer diskResources, Integer gpuResources) {
        this.cpuResources = cpuResources;
        this.memoryResources = memoryResources;
        this.diskResources = diskResources;
    }

    public ServerResources toEntity() {
        return ServerResources.builder()
                .serverId(UUID.randomUUID())
                .cpuResources(this.cpuResources)
                .memoryResources(this.memoryResources)
                .diskResources(this.diskResources)
                .build();
    }
}
