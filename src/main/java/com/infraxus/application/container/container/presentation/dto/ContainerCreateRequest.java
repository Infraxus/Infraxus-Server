package com.infraxus.application.container.container.presentation.dto;

import com.infraxus.application.container.container.domain.Container;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map; // Map import 추가
import java.util.UUID;

@Getter
@NoArgsConstructor
public class ContainerCreateRequest {
    private UUID serverId;
    private UUID deploymentId; // 새 필드 추가
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
    private String languageFramework;
    private String database;
    private String messagingSystem;
    private Map<String, String> environmentVariables; // 새 필드 추가

    @Builder
    public ContainerCreateRequest(UUID serverId, String containerName, Integer buildCount, String containerDescription, String containerState, String externalIp, String internalIp, String externalPort, String internalPort, String githubLink, String filePath, String image, String languageFramework, String database, String messagingSystem, UUID deploymentId, Map<String, String> environmentVariables) {
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
        this.languageFramework = languageFramework;
        this.database = database;
        this.messagingSystem = messagingSystem;
        this.deploymentId = deploymentId; // 생성자에 추가
        this.environmentVariables = environmentVariables; // 생성자에 추가
    }

    public Container toEntity() {
        return Container.builder()
                .containerId(UUID.randomUUID())
                .serverId(this.serverId)
                .deploymentId(this.deploymentId) // toEntity에 추가
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
