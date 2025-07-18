package com.infraxus.application.server.server.service;

import com.infraxus.global.loki.service.LoggingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DockerComposeService {

    private final LoggingService loggingService;

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
        loggingService.logEvent("Executing Docker Compose Up for: " + composeFilePath);
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("docker-compose", "-f", composeFilePath, "up", "-d");
            processBuilder.directory(new File(composeFilePath).getParentFile());
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                loggingService.logEvent("Docker Compose Up Output: " + line);
            }

            int exitCode = process.waitFor();
            if (exitCode != 0) {
                loggingService.logEvent("ERROR: Docker Compose Up failed with exit code " + exitCode);
            } else {
                loggingService.logEvent("Docker Compose Up successful.");
            }
        } catch (IOException | InterruptedException e) {
            loggingService.logEvent("ERROR: Failed to execute Docker Compose Up: " + e.getMessage());
        }
    }

    public void down(String composeFilePath) {
        loggingService.logEvent("Executing Docker Compose Down for: " + composeFilePath);
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("docker-compose", "-f", composeFilePath, "down");
            processBuilder.directory(new File(composeFilePath).getParentFile());
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                loggingService.logEvent("Docker Compose Down Output: " + line);
            }

            int exitCode = process.waitFor();
            if (exitCode != 0) {
                loggingService.logEvent("ERROR: Docker Compose Down failed with exit code " + exitCode);
            } else {
                loggingService.logEvent("Docker Compose Down successful.");
            }
        } catch (IOException | InterruptedException e) {
            loggingService.logEvent("ERROR: Failed to execute Docker Compose Down: " + e.getMessage());
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
}
