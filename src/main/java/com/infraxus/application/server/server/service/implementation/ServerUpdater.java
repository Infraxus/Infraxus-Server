package com.infraxus.application.server.server.service.implementation;

import com.infraxus.application.server.server.domain.Server;
import com.infraxus.application.server.server.domain.repository.ServerRepository;
import com.infraxus.application.server.server.presentation.dto.ServerUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ServerUpdater {

    private final ServerRepository serverRepository;

    public void update(Server updatableServer, ServerUpdateRequest newServerData){
        updatableServer.setServerType(newServerData.getServerType());
        updatableServer.setServerName(newServerData.getServerName());
        updatableServer.setServerState(newServerData.getServerState());
        updatableServer.setSkillStack(newServerData.getSkillStack());
        updatableServer.setRollBack(newServerData.getRollBack());
        updatableServer.setRebuildTime(newServerData.getRebuildTime());

        serverRepository.save(updatableServer);
    }

}
