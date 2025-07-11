package com.infraxus.application.server.resources.presentation.dto;

import com.infraxus.application.server.resources.domain.ServerResources;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ServerResourcesUpdateRequest {
    private Integer cpuResources;
    private Integer memoryResources;
    private Integer storageResources;
    private Integer gpuResources;

    @Builder
    public ServerResourcesUpdateRequest(Integer cpuResources, Integer memoryResources, Integer storageResources, Integer gpuResources) {
        this.cpuResources = cpuResources;
        this.memoryResources = memoryResources;
        this.storageResources = storageResources;
        this.gpuResources = gpuResources;
    }

    public ServerResources toEntity() {
        return ServerResources.builder()
                .cpuResources(this.cpuResources)
                .memoryResources(this.memoryResources)
                .storageResources(this.storageResources)
                .gpuResources(this.gpuResources)
                .build();
    }
}
