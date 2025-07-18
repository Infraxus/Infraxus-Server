package com.infraxus.application.container.container.service.implementation;

import com.infraxus.application.container.container.domain.Container;
import com.infraxus.application.container.container.domain.repository.ContainerRepository;
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
        return containerRepository.findAllByServerId(serverId);
    }

    public Container findByDockerContainerId(String dockerContainerId) {
        return containerRepository.findByDockerContainerId(dockerContainerId)
                .orElse(null);
    }

}
