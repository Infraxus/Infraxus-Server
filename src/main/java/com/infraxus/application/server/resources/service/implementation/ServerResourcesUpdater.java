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
        ServerResources updated = updatableServerResources.toBuilder()
                .serverId(updatableServerResources.getServerId())
                .cpuResources(newServerResourcesData.getCpuResources())
                .memoryResources(newServerResourcesData.getMemoryResources())
                .diskResources(newServerResourcesData.getDiskResources())
                .build();

        serverResourcesRepository.save(updated);
    }

}
