package com.infraxus.application.server.version.service.implementation;

import com.infraxus.application.server.version.domain.ServerVersion;
import com.infraxus.application.server.version.domain.repository.ServerVersionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ServerVersionReader {

    private final ServerVersionRepository serverVersionRepository;

    public List<ServerVersion> findAll(){
        return serverVersionRepository.findAll();
    }

    public ServerVersion findById(UUID id){
        return serverVersionRepository.findById(id)
                .orElse(null);
    }

}
