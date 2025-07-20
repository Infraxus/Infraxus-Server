package com.infraxus.global.docker;

import com.infraxus.application.alarm.alert.service.AlertingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class DockerComposeService {

    private final AlertingService alertingService;

    public void initialize(Path composeFilePath) throws IOException {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("version", "3.8");
        data.put("services", new LinkedHashMap<>());
        save(composeFilePath, data);
    }

    public void addService(Path composeFilePath, String serviceName, String imageName, String portMapping, String contextPath) throws IOException {
        Yaml yaml = new Yaml();
        Map<String, Object> data = yaml.load(composeFilePath.toFile().toURI().toURL().openStream());

        Map<String, Object> services = (Map<String, Object>) data.get("services");
        Map<String, Object> newService = new LinkedHashMap<>();
        newService.put("image", imageName);
        newService.put("container_name", serviceName);
        newService.put("ports", Collections.singletonList(portMapping));

        Map<String, String> build = new LinkedHashMap<>();
        build.put("context", contextPath);
        build.put("dockerfile", "Dockerfile");
        newService.put("build", build);

        services.put(serviceName, newService);
        save(composeFilePath, data);
    }

    public void up(String composeFilePath) {
        log.info("Executing Docker Compose Up for: {}", composeFilePath);
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("docker-compose", "-f", composeFilePath, "up", "-d");
            processBuilder.directory(new File(composeFilePath).getParentFile());
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                log.info("Docker Compose Up Output: {}", line);
            }

            int exitCode = process.waitFor();
            if (exitCode != 0) {
                log.error("ERROR: Docker Compose Up failed with exit code {}", exitCode);
            } else {
                log.info("Docker Compose Up successful.");
            }
        } catch (IOException | InterruptedException e) {
            log.error("ERROR: Failed to execute Docker Compose Up: {}", e.getMessage());
            Thread.currentThread().interrupt();
        }
    }

    public void down(String composeFilePath) {
        log.info("Executing Docker Compose Down for: {}", composeFilePath);
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("docker-compose", "-f", composeFilePath, "down");
            processBuilder.directory(new File(composeFilePath).getParentFile());
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                log.info("Docker Compose Down Output: {}", line);
            }

            int exitCode = process.waitFor();
            if (exitCode != 0) {
                log.error("ERROR: Docker Compose Down failed with exit code {}", exitCode);
            } else {
                log.info("Docker Compose Down successful.");
            }
        } catch (IOException | InterruptedException e) {
            log.error("ERROR: Failed to execute Docker Compose Down: {}", e.getMessage());
            Thread.currentThread().interrupt();
        }
    }

    private void save(Path composeFilePath, Map<String, Object> data) throws IOException {
        DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        options.setPrettyFlow(true);
        options.setIndent(2);
        options.setIndicatorIndent(2);
        options.setIndentWithIndicator(true);

        Yaml outputYaml = new Yaml(options);
        try (FileWriter writer = new FileWriter(composeFilePath.toFile())) {
            outputYaml.dump(data, writer);
        }
    }

    private String executeCommand(List<String> command) {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder(command);
            processBuilder.directory(new File(System.getProperty("user.dir"))); // Run in project root
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
                alertingService.sendAlert("Docker-compose Command Failed: " + errorMessage);
                return "Error: " + errorOutput;
            }
            return output.toString();
        } catch (IOException | InterruptedException e) {
            String errorMessage = String.format("Failed to execute command '%s': %s", command, e.getMessage());
            log.error(errorMessage);
            alertingService.sendAlert("Docker-compose Command Exception: " + errorMessage);
            Thread.currentThread().interrupt();
            return "";
        }
    }
}
