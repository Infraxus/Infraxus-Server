package com.infraxus.global.docker;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.command.InspectVolumeResponse;
import com.github.dockerjava.api.model.*;
import com.github.dockerjava.api.model.BuildResponseItem;
import com.github.dockerjava.core.command.BuildImageResultCallback;
import java.io.File;
import com.infraxus.application.alarm.alert.service.AlertingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Service
@Slf4j
@RequiredArgsConstructor
public class DockerService {

    private final DockerClient dockerClient;
    private final AlertingService alertingService;

    // --- Container Operations ---

    public List<Container> listAllContainers() {
        return dockerClient.listContainersCmd().withShowAll(true).exec();
    }

    public String createContainer(
            String imageName,
            String containerName,
            List<String> envVars,
            List<String> portBindings,
//            List<String> volumes,
            String network
    ) {
        CreateContainerResponse response = dockerClient.createContainerCmd(imageName)
                .withName(containerName)
                .withEnv(envVars)
                 .withPortBindings((PortBinding) portBindings)
                // .withVolumes(Volume.parse("/foo:/bar")) // TODO: Parse volumes
                .withHostConfig(new HostConfig().withNetworkMode(network))
                .exec();
        return response.getId();
    }

    public void startContainer(String containerId) {
        dockerClient.startContainerCmd(containerId).exec();
    }

    public void stopContainer(String containerId) {
        dockerClient.stopContainerCmd(containerId).exec();
    }

    public void restartContainer(String containerId) {
        dockerClient.restartContainerCmd(containerId).exec();
    }

    public void removeContainer(String containerId, boolean force) {
        dockerClient.removeContainerCmd(containerId).withForce(force).exec();
    }

    public InspectContainerResponse inspectContainer(String containerId) {
        return dockerClient.inspectContainerCmd(containerId).exec();
    }

    // --- Image Operations ---

    public List<Image> listAllImages() {
        return dockerClient.listImagesCmd().withShowAll(true).exec();
    }

    public void pullImage(String imageName) {
        try {
            dockerClient.pullImageCmd(imageName).start().awaitCompletion();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Image pull interrupted", e);
        }
    }

    public String buildImage(String dockerfilePath, String imageName) {
        try {
            return dockerClient.buildImageCmd(new File(dockerfilePath))
                    .withTags(Set.of(imageName))
                    .exec(new BuildImageResultCallback() {
                        @Override
                        public void onNext(BuildResponseItem item) {
                            super.onNext(item);
                            if (item.getStream() != null) {
                                log.info(item.getStream().trim());
                            }
                        }
                    })
                    .awaitImageId();
        } catch (Exception e) {
            log.error("Error building image: " + imageName, e);
            alertingService.sendAlert("Failed to build Docker image: " + imageName + ". Error: " + e.getMessage());
            return null;
        }
    }

    // --- Volume Operations ---

    public List<InspectVolumeResponse> listAllVolumes() {
        return dockerClient.listVolumesCmd().exec().getVolumes();
    }

    // --- Network Operations ---

    public List<Network> listAllNetworks() {
        return dockerClient.listNetworksCmd().exec();
    }

    // --- File Operations ---

    public void createDockerfile(String filePath, String content) throws IOException {
        Files.write(Paths.get(filePath), content.getBytes());
    }

    public void createDockerComposeFile(String filePath, String content) throws IOException {
        Files.write(Paths.get(filePath), content.getBytes());
    }
}
