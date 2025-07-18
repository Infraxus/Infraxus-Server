package com.infraxus.application.server.server.service.implementation;

import com.infraxus.application.server.server.domain.Server;
import com.infraxus.application.server.server.domain.repository.ServerRepository;
import com.infraxus.application.server.server.presentation.dto.ServerCreateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ServerCreator {

    private final ServerRepository serverRepository;

    public void save(Server server){
        serverRepository.save(server);
    }

}
