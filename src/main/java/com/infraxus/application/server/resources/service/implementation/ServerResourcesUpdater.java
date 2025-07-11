package com.infraxus.application.server.resources.service.implementation;

import com.infraxus.application.server.resources.domain.ServerResources;
import com.infraxus.application.server.resources.domain.repository.ServerResourcesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ServerResourcesUpdater {

    private final ServerResourcesRepository serverResourcesRepository;

    public void update(ServerResources updatableServerResources, ServerResources newServerResourcesData){
        updatableServerResources.toBuilder()
                .cpuResources(newServerResourcesData.getCpuResources())
                .memoryResources(newServerResourcesData.getMemoryResources())
                .storageResources(newServerResourcesData.getStorageResources())
                .gpuResources(newServerResourcesData.getGpuResources())
                .build();

        serverResourcesRepository.save(updatableServerResources);
    }

}
