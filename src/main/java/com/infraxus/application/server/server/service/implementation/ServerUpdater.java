package com.infraxus.application.server.server.service.implementation;

import com.infraxus.application.server.server.domain.Server;
import com.infraxus.application.server.server.domain.repository.ServerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ServerUpdater {

    private final ServerRepository serverRepository;

    public void update(Server updatableServer, Server newServerData){
        updatableServer.toBuilder()
                .architectureType(newServerData.getArchitectureType())
                .serverName(newServerData.getServerName())
                .serverState(newServerData.getServerState())
                .skillStack(newServerData.getSkillStack())
                .rollBack(newServerData.getRollBack())
                .rebuildTime(newServerData.getRebuildTime())
                .build();

        serverRepository.save(updatableServer);
    }

}
