package com.infraxus.application.container.container.presentation.dto;

import com.infraxus.application.container.container.domain.Container;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@NoArgsConstructor
public class ContainerCreateRequest {
    private UUID serverId;
    private String containerName;
    private Integer buildCount;
    private String containerDescription;
    private String containerState;
    private String externalIp;
    private String internalIp;
    private String externalPort;
    private String internalPort;
    private String githubLink;
    private String filePath;
    private String image;

    @Builder
    public ContainerCreateRequest(UUID serverId, String containerName, Integer buildCount, String containerDescription, String containerState, String externalIp, String internalIp, String externalPort, String internalPort, String githubLink, String filePath, String image) {
        this.serverId = serverId;
        this.containerName = containerName;
        this.buildCount = buildCount;
        this.containerDescription = containerDescription;
        this.containerState = containerState;
        this.externalIp = externalIp;
        this.internalIp = internalIp;
        this.externalPort = externalPort;
        this.internalPort = internalPort;
        this.githubLink = githubLink;
        this.filePath = filePath;
        this.image = image;
    }

    public Container toEntity() {
        return Container.builder()
                .containerId(UUID.randomUUID())
                .serverId(this.serverId)
                .containerName(this.containerName)
                .buildCount(this.buildCount)
                .containerDescription(this.containerDescription)
                .containerState(this.containerState)
                .externalIp(this.externalIp)
                .internalIp(this.internalIp)
                .externalPort(this.externalPort)
                .internalPort(this.internalPort)
                .githubLink(this.githubLink)
                .filePath(this.filePath)
                .createAt(LocalDateTime.now())
                .image(this.image)
                .build();
    }
}
