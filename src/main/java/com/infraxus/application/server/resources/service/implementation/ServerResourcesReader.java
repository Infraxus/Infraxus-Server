package com.infraxus.application.server.resources.service.implementation;

import com.infraxus.application.server.resources.domain.ServerResources;
import com.infraxus.application.server.resources.domain.repository.ServerResourcesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ServerResourcesReader {

    private final ServerResourcesRepository serverResourcesRepository;

    public List<ServerResources> findAll(){
        return serverResourcesRepository.findAll();
    }

    public ServerResources findById(UUID id){
        return serverResourcesRepository.findById(id)
                .orElse(null);
    }

}
