package com.infraxus.application.server.version.service;

import com.infraxus.application.server.version.domain.ServerVersion;
import com.infraxus.application.server.version.domain.exception.ServerVersionNotFoundException;
import com.infraxus.application.server.version.service.implementation.ServerVersionReader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class QueryServerVersionService {

    private final ServerVersionReader serverVersionReader;

    public List<ServerVersion> findAll(){
        return serverVersionReader.findAll();
    }

    public ServerVersion findById(UUID id){
        return serverVersionReader.findById(id);
    }

    public ServerVersion getServerVersionById(UUID serverId) {
        ServerVersion serverVersion = serverVersionReader.findById(serverId);
        if (serverVersion == null) {
            throw new ServerVersionNotFoundException("ServerVersion with ID " + serverId + " not found.");
        }
        return serverVersion;
    }
}
