package com.infraxus.application.container.container.service;

import com.infraxus.application.container.container.domain.Container;
import com.infraxus.application.container.container.presentation.dto.ContainerCreateRequest;
import com.infraxus.application.container.container.presentation.dto.ContainerUpdateRequest;
import com.infraxus.application.container.container.service.implementation.ContainerCreator;
import com.infraxus.application.container.container.service.implementation.ContainerDeleter;
import com.infraxus.application.container.container.service.implementation.ContainerReader;
import com.infraxus.application.container.container.service.implementation.ContainerUpdater;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CommandContainerService {

    private final ContainerCreator containerCreator;
    private final ContainerUpdater containerUpdater;
    private final ContainerDeleter containerDeleter;
    private final ContainerReader containerReader;
    private final QueryContainerService queryContainerService;

    public void createContainer(ContainerCreateRequest request){
        containerCreator.save(request.toEntity());
    }

    public void updateContainer(UUID containerId, ContainerUpdateRequest request){
        Container container = queryContainerService.getContainerById(containerId);
        containerUpdater.update(container, request.toEntity());
    }

    public void deleteContainer(UUID containerId){
        Container container = queryContainerService.getContainerById(containerId);
        containerDeleter.delete(container);
    }

}
