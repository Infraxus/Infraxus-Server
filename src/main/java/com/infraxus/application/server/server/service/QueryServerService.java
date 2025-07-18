package com.infraxus.application.server.server.service;

import com.infraxus.application.server.server.domain.Server;
import com.infraxus.application.server.server.domain.repository.ServerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class QueryServerService {

    private final ServerRepository serverRepository;

    public Server getServerById(UUID serverId) {
        return serverRepository.findById(serverId).orElseThrow(() -> new RuntimeException("Server not found"));
    }

    public List<Server> getAllServers() {
        return serverRepository.findAll();
    }
}