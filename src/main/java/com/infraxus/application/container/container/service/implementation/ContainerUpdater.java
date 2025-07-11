package com.infraxus.application.container.container.service.implementation;

import com.infraxus.application.container.container.domain.Container;
import com.infraxus.application.container.container.domain.repository.ContainerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ContainerUpdater {

    private final ContainerRepository containerRepository;

    public void update(Container updatableContainer, Container newContainerData){
        updatableContainer.toBuilder()
                .serverId(newContainerData.getServerId())
                .containerName(newContainerData.getContainerName())
                .buildCount(newContainerData.getBuildCount())
                .containerDescription(newContainerData.getContainerDescription())
                .containerState(newContainerData.getContainerState())
                .externalIp(newContainerData.getExternalIp())
                .internalIp(newContainerData.getInternalIp())
                .externalPort(newContainerData.getExternalPort())
                .internalPort(newContainerData.getInternalPort())
                .githubLink(newContainerData.getGithubLink())
                .filePath(newContainerData.getFilePath())
                .image(newContainerData.getImage())
                .build();

        containerRepository.save(updatableContainer);
    }

}
