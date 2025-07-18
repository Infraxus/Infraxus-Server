package com.infraxus.application.container.container.service;

import com.infraxus.application.container.container.domain.Container;
import com.infraxus.application.container.container.exception.ContainerNotFoundException;
import com.infraxus.application.container.container.service.implementation.ContainerReader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class QueryContainerService {

    private final ContainerReader containerReader;

    public List<Container> findAll(){
        return containerReader.findAll();
    }

    public Container findById(UUID id){
        return containerReader.findById(id);
    }

    public List<Container> findAllByServerId(UUID serverId) {
        return containerReader.findAllByServerId(serverId);
    }

    public Container getContainerById(UUID containerId) {
        Container container = containerReader.findById(containerId);
        if (container == null) {
            throw new ContainerNotFoundException("Container with ID " + containerId + " not found.");
        }
        return container;
    }

    public Container getContainerByDockerContainerId(String dockerContainerId) {
        Container container = containerReader.findByDockerContainerId(dockerContainerId);
        if (container == null) {
            throw new ContainerNotFoundException("Container with Docker ID " + dockerContainerId + " not found.");
        }
        return container;
    }
}
