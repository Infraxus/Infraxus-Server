package com.infraxus.application.server.server.service;

import com.infraxus.application.server.server.domain.Server;
import com.infraxus.application.server.server.domain.exception.ServerNotFoundException;
import com.infraxus.application.server.server.service.implementation.ServerReader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class QueryServerService {

    private final ServerReader serverReader;

    public List<Server> findAll(){
        return serverReader.findAll();
    }

    public Server findById(UUID id){
        return serverReader.findById(id);
    }

    public Server getServerById(UUID serverId) {
        Server server = serverReader.findById(serverId);
        if (server == null) {
            throw new ServerNotFoundException("Server with ID " + serverId + " not found.");
        }
        return server;
    }
}
