package com.infraxus.application.server.server.service;

import com.infraxus.application.container.domain.Container;
import com.infraxus.application.container.domain.repository.ContainerRepository;
import com.infraxus.application.container.domain.value.ContainerKey;
import com.infraxus.application.container.service.QueryContainerService;
import com.infraxus.application.container.service.implementation.ContainerUpdater;
import com.infraxus.application.server.server.domain.Server;
import com.infraxus.application.server.server.domain.repository.ServerRepository;
import com.infraxus.application.server.server.domain.value.ServerState;
import com.infraxus.application.server.server.presentation.dto.ContainerCreateRequest;
import com.infraxus.application.server.server.presentation.dto.ServerCreateRequest;
import com.infraxus.application.server.server.presentation.dto.ServerUpdateRequest;
import com.infraxus.application.server.server.service.implementation.ServerCreator;
import com.infraxus.application.server.server.service.implementation.ServerDeleter;
import com.infraxus.application.server.server.service.implementation.ServerUpdater;
import com.infraxus.global.docker.DockerService;
import com.infraxus.global.jenkins.JenkinsService;
import com.infraxus.global.jenkins.JenkinsTemplateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CommandServerService {

    private final ServerRepository serverRepository;
    private final ContainerRepository containerRepository;
    private final DockerService dockerService;
    private final JenkinsService jenkinsService;
    private final JenkinsTemplateService jenkinsTemplateService;
    private final ServerCreator serverCreator;
    private final ServerUpdater serverUpdater;
    private final ServerDeleter serverDeleter;
    private final QueryServerService queryServerService;
    private final QueryContainerService queryContainerService;
    private final ContainerUpdater containerUpdater;

    public void createServer(ServerCreateRequest request) throws IOException {
        Path serverResourcesPath = Paths.get("src/main/resources/server", request.getServerName());
        Files.createDirectories(serverResourcesPath);

        StringBuilder dockerComposeBuilder = new StringBuilder("version: '3.8'\nservices:\n");
        List<Container> newContainers = new ArrayList<>();
        Set<String> namedVolumes = new HashSet<>();

        Server server = Server.builder()
                .serverId(UUID.randomUUID())
                .serverName(request.getServerName())
                .serverType(request.getArchitectureType().name())
                .serverState(ServerState.PROVISIONING.name())
                .skillStack(request.getSkillStack())
                .rollBack(request.getRollBack())
                .createdAt(LocalDateTime.now())
                .build();

        for (ContainerCreateRequest containerRequest : request.getServerContainers()) {
            String serviceName = containerRequest.getContainerName();
            Path dockerfilePath = serverResourcesPath.resolve(serviceName + "-Dockerfile");

            // 1. Dockerfile 생성
            String dockerfileContent = generateDockerfileContent(containerRequest);
            dockerService.createDockerfile(dockerfilePath.toString(), dockerfileContent);

            // 2. Jenkins 파이프라인 생성
            String jobName = request.getServerName() + "-" + serviceName;
            Map<String, String> jenkinsParams = new HashMap<>();
            jenkinsParams.put("GIT_REPOSITORY_URL", containerRequest.getGithubLink());
            jenkinsParams.put("BRANCH", "main");
            jenkinsParams.put("DOCKER_IMAGE_NAME", containerRequest.getImage());
            String jenkinsXml = jenkinsTemplateService.createScriptFromTemplate(containerRequest.getFramework(), jenkinsParams);
            try {
                jenkinsService.createOrUpdateJob(jobName, jenkinsXml);
            } catch (IOException e) {
                System.err.println("Failed to create Jenkins job for " + jobName + ": " + e.getMessage());
            }

            // 3. Docker Compose 서비스 정의 추가 및 명명된 볼륨 수집
            List<String> serviceVolumes = new ArrayList<>();
            // If no volumes are specified, create a default one
            String defaultContainerMountPath = "/app/data"; // Default internal mount path
            String generatedVolumeName = request.getServerName().toLowerCase() + "_" + serviceName.toLowerCase() + "_default_vol";
            namedVolumes.add(generatedVolumeName);
            serviceVolumes.add(generatedVolumeName + ":" + defaultContainerMountPath);

            appendServiceToDockerCompose(dockerComposeBuilder, containerRequest, dockerfilePath.getFileName().toString(), serviceVolumes);

            // 4. 컨테이너 엔티티 준비
            newContainers.add(Container.builder()
                    .containerKey(new ContainerKey(
                            server.getServerId(),
                            UUID.randomUUID()
                    ))
                    .containerName(serviceName)
                    .containerDescription(containerRequest.getContainerDescription())
                    .containerState(ServerState.PROVISIONING.name())
                    .externalPort(containerRequest.getExternalPort())
                    .internalPort(containerRequest.getInternalPort())
                    .githubLink(containerRequest.getGithubLink())
                    .filePath(dockerfilePath.toString())
                    .image(containerRequest.getImage())
                    .createAt(LocalDateTime.now())
                    .build());
        }

        // 5. 최상위 볼륨 정의 추가
        if (!namedVolumes.isEmpty()) {
            dockerComposeBuilder.append("volumes:\n");
            for (String volumeName : namedVolumes) {
                dockerComposeBuilder.append(String.format("  %s:\n", volumeName));
            }
        }

        // 6. docker-compose.yml 파일 작성
        Path composePath = serverResourcesPath.resolve("docker-compose.yml");
        Files.write(composePath, dockerComposeBuilder.toString().getBytes());
        server.setDockerComposeFilePath(composePath.toString());

        // 7. 서버 및 컨테이너 엔티티 저장
        serverRepository.save(server);
        containerRepository.saveAll(newContainers);
    }

    private String generateDockerfileContent(ContainerCreateRequest request) {
        switch (request.getFramework().toLowerCase()) {
            case "springboot":
                return "FROM openjdk:17-jdk-slim\n" +
                       "WORKDIR /app\n" +
                       "COPY build/libs/*.jar app.jar\n" +
                       "ENTRYPOINT [\"java\",\"-jar\",\"/app.jar\"]";
            case "express":
            case "nest":
                return "FROM node:18-alpine\n" +
                       "WORKDIR /usr/src/app\n" +
                       "COPY package*.json ./\n" +
                       "RUN npm install\n" +
                       "COPY . .\n" +
                       "EXPOSE " + request.getInternalPort() + "\n" +
                       "CMD [ \"node\", \"dist/main.js\" ]";
            case "django":
            case "fastapi":
                return "FROM python:3.9-slim\n" +
                       "WORKDIR /app\n" +
                       "COPY requirements.txt requirements.txt\n" +
                       "RUN pip install --no-cache-dir -r requirements.txt\n" +
                       "COPY . .\n" +
                       "EXPOSE " + request.getInternalPort() + "\n" +
                       "CMD [\"uvicorn\", \"main:app\", \"--host\", \"0.0.0.0\", \"--port\", \"" + request.getInternalPort() + "\"]";
            default:
                return "# Dockerfile for " + request.getFramework() + " not configured. Please create it manually.";
        }
    }

    private void appendServiceToDockerCompose(StringBuilder builder, ContainerCreateRequest request, String dockerfileName, List<String> serviceVolumes) {
        builder.append(String.format("  %s:\n", request.getContainerName()));
        builder.append(String.format("    image: %s\n", request.getImage()));
        builder.append(String.format("    build:\n"));
        builder.append(String.format("      context: .\n"));
        builder.append(String.format("      dockerfile: %s\n", dockerfileName));
        builder.append(String.format("    ports:\n"));
        builder.append(String.format("      - \"%s:%s\"\n", request.getExternalPort(), request.getInternalPort()));

        if (request.getEnvVars() != null && !request.getEnvVars().isEmpty()) {
            builder.append("    environment:\n");
            request.getEnvVars().forEach(env -> builder.append(String.format("      - %s\n", env)));
        }

        if (serviceVolumes != null && !serviceVolumes.isEmpty()) {
            builder.append("    volumes:\n");
            serviceVolumes.forEach(vol -> builder.append(String.format("      - %s\n", vol)));
        }
        builder.append("\n");
    }

    public void updateServer(UUID serverId, ServerUpdateRequest request){
        Server server = queryServerService.getServerById(serverId);
        serverUpdater.update(server, request);
    }

    public void deleteServer(UUID serverId){
        Server server = queryServerService.getServerById(serverId);
        serverDeleter.delete(server);
    }

    public void startAllContainersByServerId(UUID serverId) {
        List<Container> containers = queryContainerService.findAllByServerId(serverId);
        for (Container container : containers) {
            container.setBuildStartTime(LocalDateTime.now());
            containerUpdater.update(container, container);
            dockerService.startContainer(container.getContainerKey().getContainerId().toString());
        }
    }

    public void stopAllContainersByServerId(UUID serverId) {
        List<Container> containers = queryContainerService.findAllByServerId(serverId);
        for (Container container : containers) {
            dockerService.stopContainer(container.getContainerKey().getContainerId().toString());
        }
    }
}
