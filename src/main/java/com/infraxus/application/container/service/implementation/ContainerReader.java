package com.infraxus.application.container.service.implementation;

import com.infraxus.application.container.domain.Container;
import com.infraxus.application.container.domain.repository.ContainerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ContainerReader {

    private final ContainerRepository containerRepository;

    public List<Container> findAll(){
        return containerRepository.findAll();
    }

    public Container findById(UUID id){
        return containerRepository.findById(id)
                .orElse(null);
    }

    public List<Container> findAllByServerId(UUID serverId) {
        return containerRepository.findAllByContainerKeyServerId(serverId);
    }

}
