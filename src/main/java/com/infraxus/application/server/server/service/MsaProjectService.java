package com.infraxus.application.server.server.service;

import com.infraxus.application.container.domain.Container;
import com.infraxus.application.container.domain.repository.ContainerRepository;
import com.infraxus.application.container.domain.value.ContainerKey;
import com.infraxus.application.server.resources.presentation.dto.ServerResourcesCreateRequest;
import com.infraxus.application.server.resources.service.implementation.ServerResourcesCreator;
import com.infraxus.application.server.server.presentation.dto.ContainerCreateRequest;
import com.infraxus.application.server.server.domain.Server;
import com.infraxus.application.server.server.domain.repository.ServerRepository;
import com.infraxus.application.server.server.domain.value.ServerState;
import com.infraxus.application.server.server.presentation.dto.ServerCreateRequest;
import com.infraxus.global.docker.DockerComposeService;
import com.infraxus.global.jenkins.JenkinsService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MsaProjectService {

    private final JenkinsService jenkinsService;
    private final DockerComposeService dockerComposeService;
    private final ServerRepository serverRepository;
    private final ContainerRepository containerRepository;
    private final ServerResourcesCreator serverResourcesCreator;
    private static final String MSA_PROJECTS_DIR = "msa-projects";

    public void createNewMsaProject(ServerCreateRequest request) throws IOException {
        Path projectPath = Paths.get(MSA_PROJECTS_DIR, request.getServerName());
        Files.createDirectories(projectPath);

        Path composePath = projectPath.resolve("docker-compose.yml");
        dockerComposeService.initialize(composePath);

        // 메인 애플리케이션 마이크로서비스 프로비저닝 (MsaProjectRequest의 필드 사용)
        for (ContainerCreateRequest microserviceContainer : request.getServerContainers()) {
//            provisionApplicationMicroservice(request.getArchitectureType().toString(), microserviceContainer, projectPath, request.getServerResources());
            provisionCustomMicroservice(request.getServerName(), projectPath, microserviceContainer, request.getServerResources());
        }
    }

    private void provisionInfrastructureService(String serviceType, String projectName, Path projectPath, String portMapping) throws IOException {
        Path servicePath = projectPath.resolve(serviceType);
        copyTemplateToWorkspace("msa-templates/" + serviceType, servicePath);

        String jobName = projectName + "-" + serviceType;
        String imageName = "infraxus/" + jobName;
        String pipelineScript = generateJenkinsPipelineScript(servicePath.toAbsolutePath().toString(), imageName, "Dockerfile", "./gradlew build --no-daemon");
        try {
            jenkinsService.createOrUpdateJob(jobName, pipelineScript);
            jenkinsService.triggerJob(jobName);
        } catch (IOException e) {
            System.err.println("Failed to create or trigger Jenkins job for infrastructure service: " + serviceType + ". Error: " + e.getMessage());
        }

        dockerComposeService.addService(projectPath.resolve("docker-compose.yml"), serviceType, imageName + ":latest", portMapping, servicePath.toString());
    }

    private void provisionApplicationMicroservice(String serverType, ContainerCreateRequest request, Path projectPath, ServerResourcesCreateRequest serverResourcesRequest) throws IOException {
        String serviceType = "main-app"; // 메인 애플리케이션 서비스 이름
        String serviceName = request.getContainerName() + "-" + serviceType;
        Path servicePath = projectPath.resolve(serviceType);
        copyTemplateToWorkspace("msa-templates/generic-microservice", servicePath);

        // application.yml 파일 수정 (service.name 동적 설정)
        Path applicationYmlPath = servicePath.resolve("src/main/resources/application.yml");
        String content = new String(Files.readAllBytes(applicationYmlPath), StandardCharsets.UTF_8);
        content = content.replace("${service.name}", serviceName);
        Files.write(applicationYmlPath, content.getBytes(StandardCharsets.UTF_8));

        List<String> skillStack = Arrays.asList(
                request.getLanguage(),
                request.getFramework(),
                request.getDatabase()
        );

        // Server 도메인 엔티티 생성 및 저장
        Server server = Server.builder()
                .serverId(UUID.randomUUID())
                .serverName(serviceName)
                .serverType(serverType)
                .serverState(ServerState.PROVISIONING.name())
                .skillStack(skillStack)
                .rollBack(false)
                .rebuildTime(null)
                .createdAt(LocalDateTime.now())
                .build();
        serverRepository.save(server);

        // ServerResources 엔티티 생성 및 저장
        serverResourcesCreator.save(serverResourcesRequest.toEntity().toBuilder().serverId(server.getServerId()).build());

        // Container 도메인 엔티티 생성 및 저장
        String externalPort = request.getExternalPort() != null ? request.getExternalPort() : "8080";
        String internalPort = request.getInternalPort() != null ? request.getInternalPort() : "8080";
        String portMapping = externalPort + ":" + internalPort;

        Container container = Container.builder()
                .containerKey(new ContainerKey(
                        server.getServerId(),
                        UUID.randomUUID()
                ))
                .containerName(serviceName)
                .buildCount(0)
                .containerDescription(request.getContainerDescription() != null ? request.getContainerDescription() : "Main application microservice for " + request.getContainerName())
                .containerState(ServerState.PROVISIONING.name())
                .externalIp("localhost") // 실제 배포 환경에서는 동적으로 할당
                .internalIp(serviceName) // docker-compose 내부 네트워크 이름
                .externalPort(externalPort)
                .internalPort(internalPort)
                .githubLink(request.getGithubLink() != null ? request.getGithubLink() : "N/A")
                .filePath(servicePath.toString())
                .createAt(LocalDateTime.now())
                .image(serviceName + request.getContainerName() + "-" + serviceType + ":latest")
                .build();
        containerRepository.save(container);

        // Dockerfile 생성
        Path dockerfilePath = servicePath.resolve("Dockerfile");
        String dockerfileContent = generateDockerfileContent(request);
        Files.write(dockerfilePath, dockerfileContent.getBytes());

        String jobName = request.getContainerName() + "-" + serviceType;
        String imageName = "infraxus/" + jobName;
        String pipelineScript = generateJenkinsPipelineScript(servicePath.toAbsolutePath().toString(), imageName, dockerfilePath.getFileName().toString(), "./gradlew build --no-daemon");
        try {
            jenkinsService.createOrUpdateJob(jobName, pipelineScript);
            jenkinsService.triggerJob(jobName);
        } catch (IOException e) {
            // Handle the exception, e.g., log it or throw a custom exception
            System.err.println("Failed to create or trigger Jenkins job: " + e.getMessage());
        }

        dockerComposeService.addService(projectPath.resolve("docker-compose.yml"), serviceType, imageName + ":latest", portMapping, servicePath.toString());
    }

    private void provisionCustomMicroservice(String projectName, Path projectPath, ContainerCreateRequest microserviceContainer, ServerResourcesCreateRequest serverResourcesRequest) throws IOException {
        String serviceName = projectName + "-" + microserviceContainer.getContainerName();
        Path servicePath = projectPath.resolve(microserviceContainer.getContainerName());
        copyTemplateToWorkspace("msa-templates/generic-microservice", servicePath);

        // application.yml 파일 수정 (service.name 동적 설정)
        Path applicationYmlPath = servicePath.resolve("src/main/resources/application.yml");
        String content = new String(Files.readAllBytes(applicationYmlPath), StandardCharsets.UTF_8);
        content = content.replace("${service.name}", serviceName);
        Files.write(applicationYmlPath, content.getBytes(StandardCharsets.UTF_8));

        List<String> skillStack = Arrays.asList(
                microserviceContainer.getLanguage(),
                microserviceContainer.getFramework(),
                microserviceContainer.getDatabase()
        );

        // Server 도메인 엔티티 생성 및 저장
        Server server = Server.builder()
                .serverId(UUID.randomUUID())
                .serverName(serviceName)
                .serverState(ServerState.PROVISIONING.name())
                .skillStack(skillStack)
                .rollBack(false)
                .rebuildTime(null)
                .createdAt(LocalDateTime.now())
                .build();
        serverRepository.save(server);

        // ServerResources 엔티티 생성 및 저장
        serverResourcesCreator.save(serverResourcesRequest.toEntity().toBuilder().serverId(server.getServerId()).build());

        // Container 도메인 엔티티 생성 및 저장
        String externalPort = microserviceContainer.getExternalPort() != null ? microserviceContainer.getExternalPort() : "8080";
        String internalPort = microserviceContainer.getInternalPort() != null ? microserviceContainer.getInternalPort() : "8080";
        String portMapping = externalPort + ":" + internalPort;

        Container container = Container.builder()
                .containerKey(new ContainerKey(
                        server.getServerId(),
                        UUID.randomUUID()
                ))
                .containerName(serviceName)
                .buildCount(0)
                .containerDescription(microserviceContainer.getContainerDescription() != null ? microserviceContainer.getContainerDescription() : "Custom microservice for " + projectName)
                .containerState(ServerState.PROVISIONING.name())
                .externalIp("localhost") // 실제 배포 환경에서는 동적으로 할당
                .internalIp(serviceName) // docker-compose 내부 네트워크 이름
                .externalPort(externalPort)
                .internalPort(internalPort)
                .githubLink(microserviceContainer.getGithubLink() != null ? microserviceContainer.getGithubLink() : "N/A")
                .filePath(servicePath.toString())
                .createAt(LocalDateTime.now())
                .image("infraxus/" + projectName + "-" + microserviceContainer.getContainerName() + ":latest")
                .build();
        containerRepository.save(container);

        // Dockerfile 생성
        Path dockerfilePath = servicePath.resolve("Dockerfile");
        String dockerfileContent = generateDockerfileContent(microserviceContainer);
        Files.write(dockerfilePath, dockerfileContent.getBytes());

        String jobName = projectName + "-" + microserviceContainer.getContainerName();
        String imageName = "infraxus/" + jobName;
        String buildCommand = "./gradlew build --no-daemon";

        String pipelineScript = generateJenkinsPipelineScript(servicePath.toAbsolutePath().toString(), imageName, dockerfilePath.getFileName().toString(), buildCommand);
        try {
            jenkinsService.createOrUpdateJob(jobName, pipelineScript);
            jenkinsService.triggerJob(jobName);
        } catch (IOException e) {
            // Handle the exception, e.g., log it or throw a custom exception
            System.err.println("Failed to create or trigger Jenkins job: " + e.getMessage());
        }

        dockerComposeService.addService(projectPath.resolve("docker-compose.yml"), microserviceContainer.getContainerName(), imageName + ":latest", portMapping, servicePath.toString());
    }

    private void copyTemplateToWorkspace(String templatePath, Path targetPath) throws IOException {
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource[] resources = resolver.getResources("classpath*:" + templatePath + "/**");
        Files.createDirectories(targetPath);

        for (Resource resource : resources) {
            String resourcePath = resource.getURL().getPath();
            String relativePath = resourcePath.substring(resourcePath.indexOf(templatePath) + templatePath.length());
            if (relativePath.startsWith("/")) {
                relativePath = relativePath.substring(1);
            }
            Path finalPath = targetPath.resolve(relativePath);

            if (resource.isReadable()) {
                if (resource.getURL().toString().endsWith("/") || (resource.isFile() && resource.getFilename().isEmpty())) {
                    Files.createDirectories(finalPath);
                } else {
                    Files.createDirectories(finalPath.getParent());
                    FileCopyUtils.copy(resource.getInputStream(), Files.newOutputStream(finalPath));
                }
            }
        }
    }

    private String generateJenkinsPipelineScript(String contextPath, String imageName, String dockerfilePath, String buildCommand) {
        return String.format("""
        pipeline {
            agent any
            stages {
                stage('Build') {
                    steps {
                        sh "%s"
                    }
                }
                stage('Build & Push Docker Image') {
                    steps {
                        script {
                            def customImage = docker.build('%s:latest', '-f %s %s')
                            // In a real scenario, you would push to a registry
                            // customImage.push()
                        }
                    }
                }
            }
        }
        """, buildCommand, imageName, dockerfilePath, contextPath);
    }

    private String generateDockerfileContent(ContainerCreateRequest request) {
        String framework = request.getFramework().toLowerCase();

        switch (framework) {
            case "springboot":
                return """
                   FROM openjdk:17-jdk-slim
                   WORKDIR /app
                   COPY build/libs/*.jar app.jar
                   ENTRYPOINT ["java", "-jar", "/app/app.jar"]
                   """;

            case "express":
            case "nest":
                return """
                   FROM node:18-alpine
                   WORKDIR /usr/src/app
                   COPY package*.json ./
                   RUN npm install
                   COPY . .
                   EXPOSE """ + request.getInternalPort() + """
                   CMD ["node", "dist/main.js"]
                   """;

            case "django":
                return """
                   FROM python:3.9-slim
                   WORKDIR /app
                   COPY requirements.txt .
                   RUN pip install --no-cache-dir -r requirements.txt
                   COPY . .
                   EXPOSE %s
                   CMD ["gunicorn", "projectname.wsgi:application", "--bind", "0.0.0.0:%s"]
                   """.formatted(request.getInternalPort(), request.getInternalPort());

            case "fastapi":
                return """
                   FROM python:3.9-slim
                   WORKDIR /app
                   COPY requirements.txt .
                   RUN pip install --no-cache-dir -r requirements.txt
                   COPY . .
                   EXPOSE %s
                   CMD ["uvicorn", "main:app", "--host", "0.0.0.0", "--port", "%s"]
                   """.formatted(request.getInternalPort(), request.getInternalPort());

            default:
                return "# Dockerfile for " + request.getFramework() + " not configured. Please create it manually.";
        }
    }

}
