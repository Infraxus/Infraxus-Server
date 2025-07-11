package com.infraxus.application.server.version.service.implementation;

import com.infraxus.application.server.version.domain.ServerVersion;
import com.infraxus.application.server.version.domain.repository.ServerVersionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ServerVersionCreator {

    private final ServerVersionRepository serverVersionRepository;

    public void save(ServerVersion serverVersion){
        serverVersionRepository.save(serverVersion);
    }

}
