package com.infraxus.application.server.server.service;

import com.infraxus.application.container.container.domain.Container;
import com.infraxus.application.container.container.domain.repository.ContainerRepository;
import com.infraxus.application.server.server.domain.Server;
import com.infraxus.application.server.server.domain.repository.ServerRepository;
import com.infraxus.application.server.server.domain.value.ArchitectureType;
import com.infraxus.application.server.server.presentation.dto.ServerCreateRequest;
import com.infraxus.application.server.server.presentation.dto.ServerUpdateRequest;
import com.infraxus.application.server.server.service.implementation.ServerCreator;
import com.infraxus.application.server.server.service.implementation.ServerDeleter;
import com.infraxus.application.server.server.service.implementation.ServerReader;
import com.infraxus.application.server.server.service.implementation.ServerUpdater;
import com.infraxus.global.docker.DockerService;
import com.infraxus.global.jenkins.JenkinsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.FileCopyUtils;

@Service
@RequiredArgsConstructor
public class CommandServerService {

    private final ServerRepository serverRepository;
    private final ContainerRepository containerRepository;
    private final DockerService dockerService;
    private final JenkinsService jenkinsService;

    private final ServerCreator serverCreator;
    private final ServerUpdater serverUpdater;
    private final ServerDeleter serverDeleter;
    private final ServerReader serverReader;
    private final QueryServerService queryServerService;

    private static final String MONOLITHIC_PROJECTS_DIR = "monolithic-projects";

    public void createServer(ServerCreateRequest request) throws IOException {

        Path projectPath = Paths.get(MONOLITHIC_PROJECTS_DIR, request.getServerName());
        Files.createDirectories(projectPath);

        Path jenkinsfilePath = projectPath.resolve("Jenkinsfile");
        ClassPathResource jenkinsTemplate = new ClassPathResource("jenkins-files/build/gradle.groovy");
        FileCopyUtils.copy(jenkinsTemplate.getInputStream(), Files.newOutputStream(jenkinsfilePath));

        serverCreator.save(
                Server.create(
                        request.getServerName(),
                        request.getArchitectureType().name(),
                        jenkinsfilePath.toString(),
                        null, // dockerComposeFilePath for monolithic is null
                        request.getSkillStack(),
                        request.getRollBack()
                )
        );
    }

    public void updateServer(UUID serverId, ServerUpdateRequest request){
        Server server = queryServerService.getServerById(serverId);
        serverUpdater.update(server, request);
    }

    public void deleteServer(UUID serverId){
        Server server = queryServerService.getServerById(serverId);
        serverDeleter.delete(server);
    }

    public String deployServer(UUID serverId, ArchitectureType architectureType) throws IOException {
        switch (architectureType) {
            case Monolithic:
                return deployMonolithicServer(serverId);
            case MSA:
                return deployMsaServer(serverId);
            default:
                throw new IllegalArgumentException("Unsupported architecture type");
        }
    }

    public void restartServer(UUID serverId) {
        Server server = queryServerService.getServerById(serverId);
        List<Container> containers = containerRepository.findAllByServerId(serverId);

        if (containers.isEmpty()) {
            throw new IllegalArgumentException("No containers found for server: " + server.getServerName());
        }

        for (Container container : containers) {
            dockerService.restartContainer(container, container.getDockerContainerId());
            containerRepository.save(container); // Save to persist updated buildEndTime, buildDuration
        }
    }

    private String deployMonolithicServer(UUID serverId) throws IOException {
        Server server = serverRepository.findById(serverId).orElseThrow(() -> new RuntimeException("Server not found"));
        List<Container> containers = containerRepository.findAllByServerId(serverId);
        if (containers.isEmpty()) {
            throw new RuntimeException("No containers found for monolithic server: " + server.getServerName());
        }
        Container container = containers.get(0);

        String jobName = server.getServerName() + "-" + container.getContainerName();
        String imageFullName = "infraxus/" + container.getContainerName();
        String containerName = container.getContainerName();

        // 1. Trigger Jenkins Job
        jenkinsService.triggerJob(jobName);

        // 2. Wait for Jenkins job to complete (this is a simplified approach)
        // In a real-world scenario, you would use webhooks or polling to check the job status.
        try {
            Thread.sleep(30000); // Simulating build time
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return "Deployment interrupted";
        }

        // 3. Pull the new image
        dockerService.pullImage(imageFullName);

        // 4. Stop and remove the old container
        dockerService.stopContainer(container.getDockerContainerId()); // Use Docker ID
        dockerService.removeContainer(container.getDockerContainerId(), true, false); // Use Docker ID

        // 5. Start a new container with the new image
        String metricsPort = "8081"; // Example metrics port

        // Update build start time before creating
        container.setBuildStartTime(LocalDateTime.now());
        containerRepository.save(container); // Save to persist buildStartTime

        String result = dockerService.createContainer(
                container, // Pass the existing Container object
                imageFullName,
                containerName,
                Collections.emptyList(),
                Collections.singletonList(container.getExternalPort() + ":" + container.getInternalPort()),
                Collections.emptyList(),
                metricsPort,
                null
        );
        containerRepository.save(container); // Save again to persist buildEndTime, buildDuration, dockerContainerId, containerState

        return result;
    }

    private String deployMsaServer(UUID serverId) throws IOException {
        Server server = serverRepository.findById(serverId)
                .orElseThrow(() -> new RuntimeException("Server not found"));
        List<Container> containers = containerRepository.findAllByServerId(serverId);

        if (containers.isEmpty()) {
            return "No containers to deploy for this MSA server.";
        }

        String networkName = server.getServerName().toLowerCase() + "-network";
        dockerService.createNetwork(networkName);

        StringBuilder deploymentResult = new StringBuilder();

        for (Container container : containers) {
            String jobName = server.getServerName() + "-" + container.getContainerName();
            String imageFullName = "infraxus/" + container.getContainerName();
            String containerName = container.getContainerName();

            // 1. Trigger Jenkins Job
            jenkinsService.triggerJob(jobName);

            // 2. Wait for Jenkins job to complete
            try {
                Thread.sleep(30000); // Simulate build time
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                deploymentResult.append(String.format("Deployment for %s interrupted.\n", containerName));
                continue; // Proceed to the next container
            }

            // 3. Pull the new image
            dockerService.pullImage(imageFullName);

            // 4. Stop and remove the old container
            dockerService.stopContainer(container.getDockerContainerId()); // Use Docker ID
            dockerService.removeContainer(container.getDockerContainerId(), true, false); // Use Docker ID

            // 5. Start a new container with the new image and network
            String metricsPort = "8081"; // This should be dynamic per container if needed

            // Update build start time before creating
            container.setBuildStartTime(LocalDateTime.now());
            containerRepository.save(container); // Save to persist buildStartTime

            String result = dockerService.createContainer(
                    container, // Pass the existing Container object
                    imageFullName,
                    containerName,
                    Collections.emptyList(),
                    Collections.singletonList(container.getExternalPort() + ":" + container.getInternalPort()),
                    Collections.emptyList(),
                    metricsPort,
                    networkName
            );
            containerRepository.save(container); // Save again to persist buildEndTime, buildDuration, dockerContainerId, containerState

            deploymentResult.append(String.format("Container %s deployed: %s\n", containerName, result));
        }

        return deploymentResult.toString();
    }
}
