package com.infraxus.application.server.server.service;

import com.infraxus.application.server.server.domain.Server;
import com.infraxus.application.server.server.presentation.dto.ServerCreateRequest;
import com.infraxus.application.server.server.presentation.dto.ServerUpdateRequest;
import com.infraxus.application.server.server.service.implementation.ServerCreator;
import com.infraxus.application.server.server.service.implementation.ServerDeleter;
import com.infraxus.application.server.server.service.implementation.ServerReader;
import com.infraxus.application.server.server.service.implementation.ServerUpdater;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CommandServerService {

    private final ServerCreator serverCreator;
    private final ServerUpdater serverUpdater;
    private final ServerDeleter serverDeleter;
    private final ServerReader serverReader;
    private final QueryServerService queryServerService;

    public void createServer(ServerCreateRequest request){
        serverCreator.save(request.toEntity());
    }

    public void updateServer(UUID serverId, ServerUpdateRequest request){
        Server server = queryServerService.getServerById(serverId);
        serverUpdater.update(server, request.toEntity());
    }

    public void deleteServer(UUID serverId){
        Server server = queryServerService.getServerById(serverId);
        serverDeleter.delete(server);
    }

}
