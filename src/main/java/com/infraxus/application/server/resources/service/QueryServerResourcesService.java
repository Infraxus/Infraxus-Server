package com.infraxus.application.server.resources.service;

import com.infraxus.application.server.resources.domain.ServerResources;
import com.infraxus.application.server.resources.domain.exception.ServerResourcesNotFoundException;
import com.infraxus.application.server.resources.service.implementation.ServerResourcesReader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class QueryServerResourcesService {

    private final ServerResourcesReader serverResourcesReader;

    public List<ServerResources> findAll(){
        return serverResourcesReader.findAll();
    }

    public ServerResources findById(UUID id){
        return serverResourcesReader.findById(id);
    }

    public ServerResources getServerResourcesById(UUID serverId) {
        ServerResources serverResources = serverResourcesReader.findById(serverId);
        if (serverResources == null) {
            throw new ServerResourcesNotFoundException("ServerResources with ID " + serverId + " not found.");
        }
        return serverResources;
    }
}
