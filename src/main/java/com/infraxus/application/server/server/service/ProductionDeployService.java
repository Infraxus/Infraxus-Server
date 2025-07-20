package com.infraxus.application.server.server.service;

import com.infraxus.global.jenkins.JenkinsService;
import com.infraxus.global.jenkins.JenkinsTemplateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductionDeployService {

    private final JenkinsService jenkinsService;
    private final JenkinsTemplateService jenkinsTemplateService;

    private String createJobName(String serviceName) {
        // Creates a standardized job name, e.g., "prod-my-blog-api"
        return "prod-" + serviceName.replaceAll("\s+", "-").toLowerCase();
    }
}
