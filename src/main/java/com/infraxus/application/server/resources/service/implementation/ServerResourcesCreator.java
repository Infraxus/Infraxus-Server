package com.infraxus.application.server.resources.service.implementation;

import com.infraxus.application.server.resources.domain.ServerResources;
import com.infraxus.application.server.resources.domain.repository.ServerResourcesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ServerResourcesCreator {

    private final ServerResourcesRepository serverResourcesRepository;

    public void save(ServerResources serverResources){
        serverResourcesRepository.save(serverResources);
    }

}
