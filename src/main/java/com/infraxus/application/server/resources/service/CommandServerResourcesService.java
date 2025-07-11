package com.infraxus.application.server.resources.service;

import com.infraxus.application.server.resources.domain.ServerResources;
import com.infraxus.application.server.resources.presentation.dto.ServerResourcesCreateRequest;
import com.infraxus.application.server.resources.presentation.dto.ServerResourcesUpdateRequest;
import com.infraxus.application.server.resources.service.implementation.ServerResourcesCreator;
import com.infraxus.application.server.resources.service.implementation.ServerResourcesDeleter;
import com.infraxus.application.server.resources.service.implementation.ServerResourcesReader;
import com.infraxus.application.server.resources.service.implementation.ServerResourcesUpdater;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CommandServerResourcesService {

    private final ServerResourcesCreator serverResourcesCreator;
    private final ServerResourcesUpdater serverResourcesUpdater;
    private final ServerResourcesDeleter serverResourcesDeleter;
    private final ServerResourcesReader serverResourcesReader;
    private final QueryServerResourcesService queryServerResourcesService;

    public void createServerResources(ServerResourcesCreateRequest request){
        serverResourcesCreator.save(request.toEntity());
    }

    public void updateServerResources(UUID serverId, ServerResourcesUpdateRequest request){
        ServerResources serverResources = queryServerResourcesService.getServerResourcesById(serverId);
        serverResourcesUpdater.update(serverResources, request.toEntity());
    }

    public void deleteServerResources(UUID serverId){
        ServerResources serverResources = queryServerResourcesService.getServerResourcesById(serverId);
        serverResourcesDeleter.delete(serverResources);
    }

}
