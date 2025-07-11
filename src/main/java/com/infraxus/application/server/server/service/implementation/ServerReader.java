package com.infraxus.application.server.server.service.implementation;

import com.infraxus.application.server.server.domain.Server;
import com.infraxus.application.server.server.domain.repository.ServerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ServerReader {

    private final ServerRepository serverRepository;

    public List<Server> findAll(){
        return serverRepository.findAll();
    }

    public Server findById(UUID id){
        return serverRepository.findById(id)
                .orElse(null);
    }

}
