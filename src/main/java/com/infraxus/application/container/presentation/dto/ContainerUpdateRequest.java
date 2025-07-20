package com.infraxus.application.container.presentation.dto;

import com.infraxus.application.container.domain.Container;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
public class ContainerUpdateRequest {
    private String containerName;
    private String containerDescription;
    private String externalPort;
    private String internalPort;
    private String githubLink;
    private String filePath;
    private String image;

    @Builder
    public ContainerUpdateRequest(String containerName, String containerDescription, String externalPort, String internalPort, String githubLink, String filePath, String image) {
        this.containerName = containerName;
        this.containerDescription = containerDescription;
        this.externalPort = externalPort;
        this.internalPort = internalPort;
        this.githubLink = githubLink;
        this.filePath = filePath;
        this.image = image;
    }

    public Container toEntity() {
        return Container.builder()
                .containerName(this.containerName)
                .containerDescription(this.containerDescription)
                .externalPort(this.externalPort)
                .internalPort(this.internalPort)
                .githubLink(this.githubLink)
                .filePath(this.filePath)
                .image(this.image)
                .build();
    }
}
