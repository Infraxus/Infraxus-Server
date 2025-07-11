package com.infraxus.global.docker;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DockerService {

    @PostConstruct
    public void init() {
        log.info("DockerService initialized. Using Docker CLI.");
    }

    private String executeCommand(List<String> command) {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder(command);
            Process process = processBuilder.start();

            StringBuilder output = new StringBuilder();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }

            int exitCode = process.waitFor();
            if (exitCode != 0) {
                BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
                String errorOutput = errorReader.lines().collect(Collectors.joining("\n"));
                log.error("Command execution failed with exit code {}: {}. Error: {}", exitCode, command, errorOutput);
                // Or throw a custom exception
                return "Error: " + errorOutput;
            }
            return output.toString();
        } catch (IOException | InterruptedException e) {
            log.error("Failed to execute command '{}': {}", command, e.getMessage());
            Thread.currentThread().interrupt();
            return "";
        }
    }


    // --- Container Operations ---

    public String listAllContainers() {
        return executeCommand(Arrays.asList("docker", "ps", "-a", "--format", "{{.ID}}\t{{.Names}}\t{{.Image}}\t{{.Status}}"));
    }

    public String createContainer(String imageName, String containerName, List<String> envVars, List<String> portBindings, List<String> volumes) {
        List<String> command = new ArrayList<>(Arrays.asList("docker", "run", "-d", "--name", containerName));
        if (envVars != null) {
            for (String env : envVars) {
                command.add("-e");
                command.add(env);
            }
        }
        if (portBindings != null) {
            for (String pb : portBindings) {
                command.add("-p");
                command.add(pb);
            }
        }
        if (volumes != null) {
            for (String vol : volumes) {
                command.add("-v");
                command.add(vol);
            }
        }
        command.add(imageName);
        return executeCommand(command);
    }

    public String startContainer(String containerId) {
        return executeCommand(Arrays.asList("docker", "start", containerId));
    }

    public String stopContainer(String containerId) {
        return executeCommand(Arrays.asList("docker", "stop", containerId));
    }

    public String restartContainer(String containerId) {
        return executeCommand(Arrays.asList("docker", "restart", containerId));
    }

    public String removeContainer(String containerId, boolean force, boolean removeVolumes) {
        List<String> command = new ArrayList<>(Arrays.asList("docker", "rm"));
        if (force) {
            command.add("-f");
        }
        if (removeVolumes) {
            command.add("-v");
        }
        command.add(containerId);
        return executeCommand(command);
    }

    public String inspectContainer(String containerId) {
        return executeCommand(Arrays.asList("docker", "inspect", containerId));
    }

    public String getContainerLogs(String containerId) {
        return executeCommand(Arrays.asList("docker", "logs", containerId));
    }

    // --- Image Operations ---

    public String listAllImages() {
        return executeCommand(Arrays.asList("docker", "images", "--format", "{{.ID}}\t{{.Repository}}\t{{.Tag}}\t{{.Size}}"));
    }

    public String pullImage(String imageName) {
        return executeCommand(Arrays.asList("docker", "pull", imageName));
    }

    public String buildImage(String dockerfilePath, String tagName) {
        return executeCommand(Arrays.asList("docker", "build", "-t", tagName, dockerfilePath));
    }

    public String removeImage(String imageId, boolean force, boolean noPrune) {
        List<String> command = new ArrayList<>(Arrays.asList("docker", "rmi"));
        if (force) {
            command.add("-f");
        }
        if (noPrune) {
            command.add("--no-prune");
        }
        command.add(imageId);
        return executeCommand(command);
    }

    public String inspectImage(String imageId) {
        return executeCommand(Arrays.asList("docker", "inspect", imageId));
    }

    // --- Volume Operations ---

    public String createVolume(String name) {
        return executeCommand(Arrays.asList("docker", "volume", "create", name));
    }

    public String removeVolume(String name) {
        return executeCommand(Arrays.asList("docker", "volume", "rm", name));
    }

    public String listAllVolumes() {
        return executeCommand(Arrays.asList("docker", "volume", "ls", "--format", "{{.Name}}\t{{.Driver}}\t{{.Scope}}"));
    }

    // --- Network Operations ---

    public String createNetwork(String name) {
        return executeCommand(Arrays.asList("docker", "network", "create", name));
    }

    public String removeNetwork(String networkId) {
        return executeCommand(Arrays.asList("docker", "network", "rm", networkId));
    }

    public String listAllNetworks() {
        return executeCommand(Arrays.asList("docker", "network", "ls", "--format", "{{.ID}}\t{{.Name}}\t{{.Driver}}"));
    }

    public String inspectNetwork(String networkId) {
        return executeCommand(Arrays.asList("docker", "network", "inspect", networkId));
    }
}
