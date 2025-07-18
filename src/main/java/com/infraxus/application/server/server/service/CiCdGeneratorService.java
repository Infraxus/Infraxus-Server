package com.infraxus.application.server.server.service;

import com.infraxus.application.server.server.presentation.dto.CiCdGenerateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CiCdGeneratorService {

    private static final String CI_CD_OUTPUT_DIR = "generated-ci-cd";

    public Path generateCiCdFiles(CiCdGenerateRequest request) throws IOException {
        String projectName = request.getProjectName();
        String serviceName = request.getServiceName();
        String languageFramework = request.getLanguageFramework();
        String database = request.getDatabase();
        String messagingSystem = request.getMessagingSystem();
        String exposedPort = request.getExposedPort();

        // Determine build command, base image, copy path, entrypoint based on language/framework
        Map<String, String> config = getConfigForLanguageFramework(languageFramework);
        String buildCommand = config.get("buildCommand");
        String baseImage = config.get("baseImage");
        String copyPath = config.get("copyPath");
        String entrypoint = config.get("entrypoint");
        String dockerfileName = config.getOrDefault("dockerfileName", "Dockerfile");

        // Create output directory
        Path outputDirPath = Paths.get(CI_CD_OUTPUT_DIR, projectName, serviceName);
        Files.createDirectories(outputDirPath);

        // Generate Jenkinsfile
        String jenkinsfileContent = generateJenkinsfileContent(
                projectName, serviceName, buildCommand, 
                String.format("infraxus/%s/%s", projectName, serviceName), 
                dockerfileName
        );
        Path jenkinsfilePath = outputDirPath.resolve("Jenkinsfile");
        Files.write(jenkinsfilePath, jenkinsfileContent.getBytes());

        // Generate Dockerfile
        String dockerfileContent = generateDockerfileContent(baseImage, copyPath, entrypoint, exposedPort);
        Path dockerfilePath = outputDirPath.resolve(dockerfileName);
        Files.write(dockerfilePath, dockerfileContent.getBytes());

        return outputDirPath;
    }

    private Map<String, String> getConfigForLanguageFramework(String languageFramework) {
        Map<String, String> config = new HashMap<>();
        switch (languageFramework) {
            case "Java/Spring Boot (Gradle)":
                config.put("buildCommand", "./gradlew build -x test");
                config.put("baseImage", "eclipse-temurin:17-jre-jammy");
                config.put("copyPath", "build/libs/*.jar");
                config.put("entrypoint", "[\"java\", \"-jar\", \"app.jar\"]");
                break;
            case "Java/Spring Boot (Maven)":
                config.put("buildCommand", "mvn clean package -DskipTests");
                config.put("baseImage", "eclipse-temurin:17-jre-jammy");
                config.put("copyPath", "target/*.jar");
                config.put("entrypoint", "[\"java\", \"-jar\", \"app.jar\"]");
                break;
            case "Python/Django":
                config.put("buildCommand", "pip install -r requirements.txt && python manage.py collectstatic --noinput");
                config.put("baseImage", "python:3.9-slim-buster");
                config.put("copyPath", ".");
                config.put("entrypoint", "[\"gunicorn\", \"--bind\", \"0.0.0.0:8000\", \"myproject.wsgi\"]");
                break;
            case "Python/FastAPI":
                config.put("buildCommand", "pip install -r requirements.txt");
                config.put("baseImage", "python:3.9-slim-buster");
                config.put("copyPath", ".");
                config.put("entrypoint", "[\"uvicorn\", \"main:app\", \"--host\", \"0.0.0.0\", \"--port\", \"8000\"]");
                break;
            case "Node.js/Express":
                config.put("buildCommand", "npm install && npm run build");
                config.put("baseImage", "node:18-slim");
                config.put("copyPath", ".");
                config.put("entrypoint", "[\"node\", \"dist/index.js\"]");
                break;
            case "Node.js/NestJS":
                config.put("buildCommand", "npm install && npm run build");
                config.put("baseImage", "node:18-slim");
                config.put("copyPath", ".");
                config.put("entrypoint", "[\"node\", \"dist/main.js\"]");
                break;
            case "Go/Gin":
                config.put("buildCommand", "go mod tidy && go build -o main .");
                config.put("baseImage", "golang:1.20-alpine");
                config.put("copyPath", ".");
                config.put("entrypoint", "[\".\\/main\"]");
                config.put("dockerfileName", "Dockerfile.build");
                break;
            default:
                throw new IllegalArgumentException("Unsupported language/framework: " + languageFramework);
        }
        return config;
    }

    private String generateJenkinsfileContent(String projectName, String serviceName, String buildCommand, String imageName, String dockerfileName) {
        return String.format("""
        pipeline {
            agent any
            environment {
                PROJECT_NAME = "%s"
                SERVICE_NAME = "%s"
                DOCKER_IMAGE_NAME = "%s"
                DOCKER_IMAGE_TAG = "latest"
                BUILD_COMMAND = "%s"
                DOCKERFILE_PATH = "%s"
            }
            stages {
                stage('Checkout') {
                    steps {
                        checkout scm
                    }
                }
                stage('Build') {
                    steps {
                        sh "${BUILD_COMMAND}"
                    }
                }
                stage('Build & Push Docker Image') {
                    steps {
                        script {
                            def customImage = docker.build("${DOCKER_IMAGE_NAME}:${DOCKER_IMAGE_TAG}", "-f ${DOCKERFILE_PATH} .")
                            // In a real scenario, you would push to a registry
                            // customImage.push()
                        }
                    }
                }
                stage('Deploy') {
                    steps {
                        echo "Deployment logic goes here. Example: kubectl apply -f k8s-deployment.yaml"
                        // Example: sh 'kubectl apply -f k8s-deployment.yaml'
                    }
                }
            }
            post {
                always {
                    cleanWs()
                }
            }
        }
        """, projectName, serviceName, imageName, buildCommand, dockerfileName);
    }

    private String generateDockerfileContent(String baseImage, String copyPath, String entrypoint, String exposedPort) {
        StringBuilder dockerfileContent = new StringBuilder();
        dockerfileContent.append(String.format("FROM %s\n", baseImage));
        dockerfileContent.append("WORKDIR /app\n");
        dockerfileContent.append(String.format("COPY %s .\n", copyPath));
        if (exposedPort != null && !exposedPort.isEmpty()) {
            dockerfileContent.append(String.format("EXPOSE %s\n", exposedPort));
        }
        dockerfileContent.append(String.format("ENTRYPOINT %s\n", entrypoint));
        return dockerfileContent.toString();
    }
}
