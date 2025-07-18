package com.infraxus.global.docker;

import com.infraxus.application.container.container.domain.Container;
import com.infraxus.application.alarm.alert.service.AlertingService;
import com.infraxus.global.loki.service.LoggingService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class DockerService {

    private final LoggingService loggingService;
    private final AlertingService alertingService;

    @PostConstruct
    public void init() {
        log.info("DockerService initialized. Using Docker CLI.");
        loggingService.logEvent("DockerService initialized.");
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
                String errorMessage = String.format("Command execution failed with exit code %d: %s. Error: %s", exitCode, command, errorOutput);
                log.error(errorMessage);
                loggingService.logEvent("ERROR: " + errorMessage);
                alertingService.sendAlert("Docker Command Failed: " + errorMessage);
                return "Error: " + errorOutput;
            }
            return output.toString();
        } catch (IOException | InterruptedException e) {
            String errorMessage = String.format("Failed to execute command '%s': %s", command, e.getMessage());
            log.error(errorMessage);
            loggingService.logEvent("ERROR: " + errorMessage);
            alertingService.sendAlert("Docker Command Exception: " + errorMessage);
            Thread.currentThread().interrupt();
            return "";
        }
    }


    // --- Container Operations ---

    public String listAllContainers() {
        loggingService.logEvent("Listing all Docker containers.");
        return executeCommand(Arrays.asList("docker", "ps", "-a", "--format", "{{.ID}}\t{{.Names}}\t{{.Image}}\t{{.Status}}"));
    }

    public String listContainersByLabel(String labelFilter) {
        loggingService.logEvent("Listing Docker containers by label: " + labelFilter);
        return executeCommand(Arrays.asList("docker", "ps", "-a", "--filter", "label=" + labelFilter, "--format", "{{.ID}}\t{{.Names}}\t{{.Image}}\t{{.Status}}"));
    }

    public String createContainer(Container container, String imageName, String containerName, List<String> envVars, List<String> portBindings, List<String> volumes, String metricsPort, String network) {
        long startTime = System.currentTimeMillis(); // 빌드 시작 시간 기록
        loggingService.logEvent("Attempting to create Docker container: " + containerName + " from image: " + imageName);
        List<String> command = new ArrayList<>(Arrays.asList("docker", "run", "-d", "--name", containerName));

        // Add labels for Prometheus service discovery
        command.add("--label");
        command.add("prometheus.scrape=true");
        command.add("--label");
        command.add("prometheus.port=" + metricsPort);

        if (network != null && !network.isEmpty()) {
            command.add("--network");
            command.add(network);
        }

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
        String result = executeCommand(command);
        if (!result.startsWith("Error")) {
            Long duration = System.currentTimeMillis() - startTime;
            loggingService.logEvent("Successfully created Docker container: " + containerName);
            container.setBuildEndTime(LocalDateTime.now());
            container.setBuildDuration(duration);
            container.setDockerContainerId(result.trim()); // Docker 컨테이너 ID 설정
            container.setContainerState("RUNNING"); // 컨테이너 상태를 RUNNING으로 설정
        } else {
            alertingService.sendAlert("Failed to create Docker container: " + containerName + ". Error: " + result);
            container.setContainerState("ERROR"); // 컨테이너 상태를 ERROR로 설정
        }
        return result;
    }

    public String startContainer(String containerId) {
        loggingService.logEvent("Attempting to start Docker container: " + containerId);
        String result = executeCommand(Arrays.asList("docker", "start", containerId));
        if (!result.startsWith("Error")) {
            loggingService.logEvent("Successfully started Docker container: " + containerId);
        } else {
            alertingService.sendAlert("Failed to start Docker container: " + containerId + ". Error: " + result);
        }
        return result;
    }

    public String stopContainer(String containerId) {
        loggingService.logEvent("Attempting to stop Docker container: " + containerId);
        String result = executeCommand(Arrays.asList("docker", "stop", containerId));
        if (!result.startsWith("Error")) {
            loggingService.logEvent("Successfully stopped Docker container: " + containerId);
        } else {
            alertingService.sendAlert("Failed to stop Docker container: " + containerId + ". Error: " + result);
        }
        return result;
    }

    public String restartContainer(Container container, String containerId) {
        long startTime = System.currentTimeMillis(); // 재시작 시작 시간 기록
        loggingService.logEvent("Attempting to restart Docker container: " + containerId);
        String result = executeCommand(Arrays.asList("docker", "restart", containerId));
        long endTime = System.currentTimeMillis(); // 재시작 종료 시간 기록
        long duration = endTime - startTime; // 재시작 소요 시간 계산

        if (!result.startsWith("Error")) {
            loggingService.logEvent("Successfully restarted Docker container: " + containerId);
            container.setBuildEndTime(LocalDateTime.now());
            container.setBuildDuration(duration);
        } else {
            alertingService.sendAlert("Failed to restart Docker container: " + containerId + ". Error: " + result);
        }
        return result;
    }

    public String removeContainer(String containerId, boolean force, boolean removeVolumes) {
        loggingService.logEvent("Attempting to remove Docker container: " + containerId);
        List<String> command = new ArrayList<>(Arrays.asList("docker", "rm"));
        if (force) {
            command.add("-f");
        }
        if (removeVolumes) {
            command.add("-v");
        }
        command.add(containerId);
        String result = executeCommand(command);
        if (!result.startsWith("Error")) {
            loggingService.logEvent("Successfully removed Docker container: " + containerId);
        } else {
            alertingService.sendAlert("Failed to remove Docker container: " + containerId + ". Error: " + result);
        }
        return result;
    }

    public String inspectContainer(String containerId) {
        loggingService.logEvent("Inspecting Docker container: " + containerId);
        return executeCommand(Arrays.asList("docker", "inspect", containerId));
    }

    public String getContainerLogs(String containerId) {
        loggingService.logEvent("Getting logs for Docker container: " + containerId);
        return executeCommand(Arrays.asList("docker", "logs", containerId));
    }

    // --- Image Operations ---

    public String listAllImages() {
        loggingService.logEvent("Listing all Docker images.");
        return executeCommand(Arrays.asList("docker", "images", "--format", "{{.ID}}\t{{.Repository}}\t{{.Tag}}\t{{.Size}}"));
    }

    public String pullImage(String imageName) {
        loggingService.logEvent("Pulling Docker image: " + imageName);
        String result = executeCommand(Arrays.asList("docker", "pull", imageName));
        if (!result.startsWith("Error")) {
            loggingService.logEvent("Successfully pulled Docker image: " + imageName);
        } else {
            alertingService.sendAlert("Failed to pull Docker image: " + imageName + ". Error: " + result);
        }
        return result;
    }

    public long buildImage(String dockerfilePath, String tagName) {
        long startTime = System.currentTimeMillis();
        loggingService.logEvent("Building Docker image: " + tagName + " from path: " + dockerfilePath);
        String result = executeCommand(Arrays.asList("docker", "build", "-t", tagName, dockerfilePath));
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        if (!result.startsWith("Error")) {
            loggingService.logEvent("Successfully built Docker image: " + tagName + " in " + duration + " ms.");
            return duration;
        } else {
            alertingService.sendAlert("Failed to build Docker image: " + tagName + ". Error: " + result);
            return -1; // Indicate failure
        }
    }

    public String removeImage(String imageId, boolean force, boolean noPrune) {
        loggingService.logEvent("Removing Docker image: " + imageId);
        List<String> command = new ArrayList<>(Arrays.asList("docker", "rmi"));
        if (force) {
            command.add("-f");
        }
        if (noPrune) {
            command.add("--no-prune");
        }
        command.add(imageId);
        String result = executeCommand(command);
        if (!result.startsWith("Error")) {
            loggingService.logEvent("Successfully removed Docker image: " + imageId);
        } else {
            alertingService.sendAlert("Failed to remove Docker image: " + imageId + ". Error: " + result);
        }
        return result;
    }

    public String inspectImage(String imageId) {
        loggingService.logEvent("Inspecting Docker image: " + imageId);
        return executeCommand(Arrays.asList("docker", "inspect", imageId));
    }

    // --- Volume Operations ---

    public String createVolume(String name) {
        loggingService.logEvent("Creating Docker volume: " + name);
        String result = executeCommand(Arrays.asList("docker", "volume", "create", name));
        if (!result.startsWith("Error")) {
            loggingService.logEvent("Successfully created Docker volume: " + name);
        } else {
            alertingService.sendAlert("Failed to create Docker volume: " + name + ". Error: " + result);
        }
        return result;
    }

    public String removeVolume(String name) {
        loggingService.logEvent("Removing Docker volume: " + name);
        String result = executeCommand(Arrays.asList("docker", "volume", "rm", name));
        if (!result.startsWith("Error")) {
            loggingService.logEvent("Successfully removed Docker volume: " + name);
        } else {
            alertingService.sendAlert("Failed to remove Docker volume: " + name + ". Error: " + result);
        }
        return result;
    }

    public String listAllVolumes() {
        loggingService.logEvent("Listing all Docker volumes.");
        return executeCommand(Arrays.asList("docker", "volume", "ls", "--format", "{{.Name}}\t{{.Driver}}\t{{.Scope}}"));
    }

    // --- Network Operations ---

    public String createNetwork(String name) {
        loggingService.logEvent("Creating Docker network: " + name);
        String result = executeCommand(Arrays.asList("docker", "network", "create", name));
        if (!result.startsWith("Error")) {
            loggingService.logEvent("Successfully created Docker network: " + name);
        } else {
            alertingService.sendAlert("Failed to create Docker network: " + name + ". Error: " + result);
        }
        return result;
    }

    public String removeNetwork(String networkId) {
        loggingService.logEvent("Removing Docker network: " + networkId);
        String result = executeCommand(Arrays.asList("docker", "network", "rm", networkId));
        if (!result.startsWith("Error")) {
            loggingService.logEvent("Successfully removed Docker network: " + networkId);
        } else {
            alertingService.sendAlert("Failed to remove Docker network: " + networkId + ". Error: " + result);
        }
        return result;
    }

    public String listAllNetworks() {
        loggingService.logEvent("Listing all Docker networks.");
        return executeCommand(Arrays.asList("docker", "network", "ls", "--format", "{{.ID}}\t{{.Name}}\t{{.Driver}}"));
    }

    public String inspectNetwork(String networkId) {
        loggingService.logEvent("Inspecting Docker network: " + networkId);
        return executeCommand(Arrays.asList("docker", "network", "inspect", networkId));
    }

    public String getContainerIpAddress(String containerId) {
        loggingService.logEvent("Getting IP address for container: " + containerId);
        String inspectOutput = inspectContainer(containerId);
        // Parse the inspect output to find the IP address
        // This is a simplified parsing. In a real application, a JSON parser would be better.
        // Example output snippet: "IPAddress": "172.17.0.2"
        String ipAddress = null;
        for (String line : inspectOutput.split("\n")) {
            if (line.contains("\"IPAddress\":")) {
                ipAddress = line.split(":")[1].trim().replace("\"", "").replace(",", "");
                break;
            }
        }
        if (ipAddress == null || ipAddress.isEmpty()) {
            loggingService.logEvent("WARNING: Could not find IP address for container: " + containerId);
        } else {
            loggingService.logEvent("Found IP address for container " + containerId + ": " + ipAddress);
        }
        return ipAddress;
    }
}
