package com.infraxus.application.container.presentation.dto;

import com.infraxus.application.container.domain.Container;
import com.infraxus.application.container.domain.value.ContainerKey;
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
    private String containerName;
    private String containerDescription;
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
    public ContainerCreateRequest(UUID serverId, String containerName, String containerDescription, String externalPort, String internalPort, String githubLink, String filePath, String image, String languageFramework, String database, String messagingSystem, Map<String, String> environmentVariables) {
        this.serverId = serverId;
        this.containerName = containerName;
        this.containerDescription = containerDescription;
        this.externalPort = externalPort;
        this.internalPort = internalPort;
        this.githubLink = githubLink;
        this.filePath = filePath;
        this.image = image;
        this.languageFramework = languageFramework;
        this.database = database;
        this.messagingSystem = messagingSystem;
        this.environmentVariables = environmentVariables; // 생성자에 추가
    }

    public Container toEntity() {
        return Container.builder()
                .containerKey(new ContainerKey(
                        serverId,
                        UUID.randomUUID()
                ))
                .containerName(this.containerName)
                .containerDescription(this.containerDescription)
                .externalPort(this.externalPort)
                .internalPort(this.internalPort)
                .githubLink(this.githubLink)
                .filePath(this.filePath)
                .createAt(LocalDateTime.now())
                .image(this.image)
                .build();
    }
}
