package com.infraxus.application.server.server.service;

import com.infraxus.application.server.resources.domain.ServerResources;
import com.infraxus.application.server.server.domain.Server;
import com.infraxus.application.server.server.domain.repository.ServerRepository;
import com.infraxus.application.server.server.presentation.dto.ServerCreateRequest;
import com.infraxus.application.server.server.presentation.dto.ServerDetailsResponse;
import com.infraxus.application.server.server.presentation.dto.ServerUpdateRequest;
import com.infraxus.global.docker.DockerService;
import com.infraxus.global.jenkins.JenkinsService;
import com.infraxus.global.loki.service.LoggingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ServerService {

    private final ServerRepository serverRepository;
    private final com.infraxus.application.server.resources.domain.repository.ServerResourcesRepository serverResourcesRepository;
    private final JenkinsService jenkinsService;
    private final DockerService dockerService;
    private final LoggingService loggingService;

    public Server createServer(ServerCreateRequest request) {
        loggingService.logEvent("Attempting to create server: " + request.getServerName() + " of type: " + request.getArchitectureType());

        // 2. Create Server entity
        Server server = Server.create(
                request.getServerName(),
                request.getArchitectureType().toString(),
                request.getJenkinsfilePath(),
                request.getDockerComposeFilePath(),
                request.getSkillStack(),
                request.getRollBack()
        );
        serverRepository.save(server);

        // 5. Create ServerResources entity
        com.infraxus.application.server.resources.domain.ServerResources serverResources = com.infraxus.application.server.resources.domain.ServerResources.builder()
                .serverId(server.getServerId())
                .cpuResources(request.getServerResources().getCpuResources())
                .memoryResources(request.getServerResources().getMemoryResources())
                .diskResources(request.getServerResources().getDiskResources())
                .build();
        serverResourcesRepository.save(serverResources);

        // 6. Trigger Jenkins CI/CD
        try {
            jenkinsService.triggerJob(request.getJenkinsfilePath());
            loggingService.logEvent("Triggered Jenkins job: " + request.getJenkinsfilePath() + " for server: " + request.getServerName());
        } catch (IOException e) {
            loggingService.logEvent("ERROR: Failed to trigger Jenkins job: " + request.getJenkinsfilePath() + ". Error: " + e.getMessage());
            // Optionally, throw an exception or handle the error more gracefully
        }

        // 7. Docker/Docker Compose operations (handled by DeploymentService and Reconciler)
        // The actual container creation is handled by DeploymentReconcilerService
        // which uses DockerService based on the Deployment object.

        loggingService.logEvent("Server created successfully: " + request.getServerName() + " (ID: " + server.getServerId() + ")");
        return server;
    }

    public ServerDetailsResponse getServerDetails(UUID serverId) {
        loggingService.logEvent("Attempting to retrieve server details for ID: " + serverId);

        Server server = serverRepository.findById(serverId)
                .orElseThrow(() -> new RuntimeException("Server not found with ID: " + serverId));
        ServerResources serverResources = serverResourcesRepository.findById(serverId)
                .orElseThrow(() -> new RuntimeException("ServerResources not found for server ID: " + serverId));

        loggingService.logEvent("Successfully retrieved server details for ID: " + serverId);
        return ServerDetailsResponse.fromEntities(server, serverResources);
    }

    public Server updateServer(UUID serverId, ServerUpdateRequest request) {
        loggingService.logEvent("Attempting to update server: " + serverId);

        Server server = serverRepository.findById(serverId)
                .orElseThrow(() -> new RuntimeException("Server not found with ID: " + serverId));
        ServerResources serverResources = serverResourcesRepository.findById(serverId)
                .orElseThrow(() -> new RuntimeException("ServerResources not found for server ID: " + serverId));

        boolean serverChanged = false;
        boolean deploymentChanged = false;
        boolean serverResourcesChanged = false;

        // Update Server entity
        if (request.getServerName() != null && !request.getServerName().equals(server.getServerName())) {
            server.setServerName(request.getServerName());
            serverChanged = true;
        }
        if (request.getServerType() != null && !request.getServerType().equals(server.getServerType())) {
            server.setServerType(request.getServerType());
            serverChanged = true;
        }
        if (request.getJenkinsfilePath() != null && !request.getJenkinsfilePath().equals(server.getJenkinsfilePath())) {
            server.setJenkinsfilePath(request.getJenkinsfilePath());
            serverChanged = true;
            // Trigger Jenkins job update if needed (more complex, just logging for now)
            loggingService.logEvent("Jenkinsfile path changed for server " + serverId + ". Consider updating Jenkins job configuration.");
        }
        if (request.getDockerComposeFilePath() != null && !request.getDockerComposeFilePath().equals(server.getDockerComposeFilePath())) {
            server.setDockerComposeFilePath(request.getDockerComposeFilePath());
            serverChanged = true;
        }
        if (request.getServerState() != null && !request.getServerState().equals(server.getServerState())) {
            server.setServerState(request.getServerState());
            serverChanged = true;
        }
        if (request.getSkillStack() != null && !request.getSkillStack().equals(server.getSkillStack())) {
            server.setSkillStack(request.getSkillStack());
            serverChanged = true;
        }
        if (request.getRollBack() != null && !request.getRollBack().equals(server.getRollBack())) {
            server.setRollBack(request.getRollBack());
            serverChanged = true;
        }
        if (request.getRebuildTime() != null && !request.getRebuildTime().equals(server.getRebuildTime())) {
            server.setRebuildTime(request.getRebuildTime());
            serverChanged = true;
        }

        // Update ServerResources entity
        if (request.getCpuResource() != null && !request.getCpuResource().equals(serverResources.getCpuResources())) {
            serverResources.setCpuResources(request.getCpuResource());
            serverResourcesChanged = true;
        }
        if (request.getMemoryResource() != null && !request.getMemoryResource().equals(serverResources.getMemoryResources())) {
            serverResources.setMemoryResources(request.getMemoryResource());
            serverResourcesChanged = true;
        }
        if (request.getStorageResource() != null && !request.getStorageResource().equals(serverResources.getDiskResources())) {
            serverResources.setDiskResources(request.getStorageResource());
            serverResourcesChanged = true;
        }

        // Save updated entities
        if (serverChanged) {
            server.setUpdatedAt(LocalDateTime.now());
            serverRepository.save(server);
            loggingService.logEvent("Server entity updated for ID: " + serverId);
        }
        if (serverResourcesChanged) {
            serverResourcesRepository.save(serverResources);
            loggingService.logEvent("ServerResources entity updated for ID: " + serverId);
        }

        // Update ServerResources entity
        if (request.getCpuResource() != null && !request.getCpuResource().equals(serverResources.getCpuResources())) {
            serverResources.setCpuResources(request.getCpuResource());
            serverResourcesChanged = true;
        }
        if (request.getMemoryResource() != null && !request.getMemoryResource().equals(serverResources.getMemoryResources())) {
            serverResources.setMemoryResources(request.getMemoryResource());
            serverResourcesChanged = true;
        }
        if (request.getStorageResource() != null && !request.getStorageResource().equals(serverResources.getDiskResources())) {
            serverResources.setDiskResources(request.getStorageResource());
            serverResourcesChanged = true;
        }

        loggingService.logEvent("Server update completed for ID: " + serverId);
        return server;
    }

    public void deleteServer(UUID serverId) {
        loggingService.logEvent("Attempting to delete server: " + serverId);

        Server server = serverRepository.findById(serverId)
                .orElseThrow(() -> new RuntimeException("Server not found with ID: " + serverId));

        // 5. Delete ServerResources entity
        serverResourcesRepository.deleteById(serverId);
        loggingService.logEvent("ServerResources deleted for ID: " + serverId);

        // 6. Delete Server entity
        serverRepository.deleteById(serverId);
        loggingService.logEvent("Server deleted successfully: " + serverId);
    }
}
