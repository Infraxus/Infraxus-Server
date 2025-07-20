package com.infraxus.application.setting.service;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class PipelineInitializer implements CommandLineRunner {

    private final PipelineManagementService pipelineManagementService;

    public PipelineInitializer(PipelineManagementService pipelineManagementService) {
        this.pipelineManagementService = pipelineManagementService;
    }

    @Override
    public void run(String... args) throws Exception {
        // List<String> frameworksToBuild = Arrays.asList(
        //     "springboot",
        //     "fastapi",
        //     "django",
        //     "nest",
        //     "express"
        // );

        // for (String framework : frameworksToBuild) {
        //     String jobName = "infraxus-" + framework + "-pipeline";
        //     String dockerImageName = "infraxus/" + framework + "-app";
        //     String dockerImageTag = "1.0.0"; // Or dynamically generate this

        //     try {
        //         pipelineManagementService.createCiCdPipeline(
        //             jobName,
        //             framework,
        //             dockerImageName,
        //             dockerImageTag
        //         );
        //     } catch (Exception e) {
        //         System.err.println("Failed to create pipeline for " + framework + ": " + e.getMessage());
        //         // Optionally re-throw or handle more gracefully
        //     }
        // }
    }
}
