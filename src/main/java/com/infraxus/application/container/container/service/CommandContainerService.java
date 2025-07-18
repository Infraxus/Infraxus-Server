package com.infraxus.application.container.container.service;

import com.infraxus.application.container.container.domain.Container;
import com.infraxus.application.container.container.presentation.dto.ContainerCreateRequest;
import com.infraxus.application.container.container.presentation.dto.ContainerUpdateRequest;
import com.infraxus.application.container.container.service.implementation.ContainerCreator;
import com.infraxus.application.container.container.service.implementation.ContainerDeleter;
import com.infraxus.application.container.container.service.implementation.ContainerReader;
import com.infraxus.application.container.container.service.implementation.ContainerUpdater;
import com.infraxus.application.server.server.presentation.dto.CiCdGenerateRequest;
import com.infraxus.application.server.server.service.CiCdGeneratorService;
import com.infraxus.global.docker.DockerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class CommandContainerService {

    private final ContainerCreator containerCreator;
    private final ContainerUpdater containerUpdater;
    private final ContainerDeleter containerDeleter;
    private final ContainerReader containerReader;
    private final QueryContainerService queryContainerService;
    private final CiCdGeneratorService ciCdGeneratorService;
    private final DockerService dockerService;

    public void createContainer(ContainerCreateRequest request) throws IOException {
        Container container = request.toEntity();
        container.setBuildStartTime(LocalDateTime.now()); // 빌드 시작 시간 설정
        containerCreator.save(container);

        // Generate CI/CD files
        CiCdGenerateRequest ciCdRequest = CiCdGenerateRequest.builder()
                .projectName("infraxus") // Assuming a default project name for now
                .serviceName(request.getContainerName())
                .languageFramework(request.getLanguageFramework())
                .database(request.getDatabase())
                .messagingSystem(request.getMessagingSystem())
                .exposedPort(request.getExternalPort())
                .build();
        Path generatedDirPath = ciCdGeneratorService.generateCiCdFiles(ciCdRequest);

        // Build Docker Image
        String imageTag = String.format("infraxus/%s/%s", ciCdRequest.getProjectName(), ciCdRequest.getServiceName());
        dockerService.buildImage(generatedDirPath.toString(), imageTag); // Dockerfile이 생성된 경로를 전달

        // Convert Map<String, String> to List<String> for DockerService
        List<String> envVars = null;
        if (request.getEnvironmentVariables() != null && !request.getEnvironmentVariables().isEmpty()) {
            envVars = new ArrayList<>();
            for (Map.Entry<String, String> entry : request.getEnvironmentVariables().entrySet()) {
                envVars.add(entry.getKey() + "=" + entry.getValue());
            }
        }

        // Create and Run Docker Container
        dockerService.createContainer(
                container, // Container 객체 전달
                imageTag,
                request.getContainerName(),
                envVars, // 환경 변수 전달
                Arrays.asList(request.getExternalPort() + ":" + request.getInternalPort()), // portBindings
                null, // volumes (필요시 request에서 가져오도록 수정)
                "9090", // metricsPort (임시값, 필요시 설정)
                null // network (필요시 설정)
        );

        // Container 객체는 dockerService.createContainer 내부에서 buildEndTime과 buildDuration이 업데이트됨
        containerUpdater.update(container, container); // 최종 업데이트된 컨테이너 저장
    }

    public void updateContainer(UUID containerId, ContainerUpdateRequest request){
        Container container = queryContainerService.getContainerById(containerId);
        containerUpdater.update(container, request.toEntity());
    }

    public void deleteContainer(UUID containerId){
        Container container = queryContainerService.getContainerById(containerId);
        containerDeleter.delete(container);
    }

    public void restartContainer(UUID containerId) {
        Container container = queryContainerService.getContainerById(containerId);
        container.setBuildStartTime(LocalDateTime.now()); // 재시작 시 빌드 시작 시간 업데이트
        containerUpdater.update(container, container); // 업데이트된 컨테이너 저장
        dockerService.restartContainer(container, container.getContainerId().toString());
    }

}
