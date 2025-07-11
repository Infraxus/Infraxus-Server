package com.infraxus.application.server.server.service.implementation;

import com.infraxus.application.server.server.domain.Server;
import com.infraxus.application.server.server.domain.repository.ServerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ServerDeleter {

    private final ServerRepository serverRepository;

    public void delete(Server server){
        serverRepository.delete(server);
    }

}
