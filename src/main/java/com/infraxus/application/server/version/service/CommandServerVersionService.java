package com.infraxus.application.server.version.service;

import com.infraxus.application.server.version.domain.ServerVersion;
import com.infraxus.application.server.version.presentation.dto.ServerVersionCreateRequest;
import com.infraxus.application.server.version.presentation.dto.ServerVersionUpdateRequest;
import com.infraxus.application.server.version.service.implementation.ServerVersionCreator;
import com.infraxus.application.server.version.service.implementation.ServerVersionDeleter;
import com.infraxus.application.server.version.service.implementation.ServerVersionReader;
import com.infraxus.application.server.version.service.implementation.ServerVersionUpdater;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CommandServerVersionService {

    private final ServerVersionCreator serverVersionCreator;
    private final ServerVersionUpdater serverVersionUpdater;
    private final ServerVersionDeleter serverVersionDeleter;
    private final ServerVersionReader serverVersionReader;
    private final QueryServerVersionService queryServerVersionService;

    public void createServerVersion(ServerVersionCreateRequest request){
        serverVersionCreator.save(request.toEntity());
    }

    public void updateServerVersion(UUID serverId, ServerVersionUpdateRequest request){
        ServerVersion serverVersion = queryServerVersionService.getServerVersionById(serverId);
        serverVersionUpdater.update(serverVersion, request.toEntity());
    }

    public void deleteServerVersion(UUID serverId){
        ServerVersion serverVersion = queryServerVersionService.getServerVersionById(serverId);
        serverVersionDeleter.delete(serverVersion);
    }

}
