package com.infraxus.application.setting.service;

import com.infraxus.global.jenkins.JenkinsService;
import com.infraxus.global.jenkins.JenkinsTemplateService;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class PipelineManagementService {

    private final JenkinsService jenkinsService;
    private final JenkinsTemplateService jenkinsTemplateService;

    private static final String WORKFLOW_JOB_PLUGIN_VERSION = "2.44";
    private static final String WORKFLOW_CPS_PLUGIN_VERSION = "2.92";

    public PipelineManagementService(JenkinsService jenkinsService, JenkinsTemplateService jenkinsTemplateService) {
        this.jenkinsService = jenkinsService;
        this.jenkinsTemplateService = jenkinsTemplateService;
    }

    /**
     * Creates a Jenkins CI/CD pipeline job for a given language/framework.
     *
     * @param jobName           The name of the Jenkins job to be created.
     * @param languageFramework The language/framework (e.g., "springboot", "fastapi").
     * @param dockerImageName   The name of the Docker image to build.
     * @param dockerImageTag    The tag for the Docker image.
     * @throws IOException If an I/O error occurs during Jenkins API calls or template processing.
     */
    public void createCiCdPipeline(String jobName, String languageFramework, String dockerImageName, String dockerImageTag) throws IOException {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("DOCKER_IMAGE_NAME", dockerImageName);
        parameters.put("DOCKER_IMAGE_TAG", dockerImageTag);

        String jenkinsfileScript = jenkinsTemplateService.createScriptFromTemplate(languageFramework, parameters);

        StringBuilder jobXmlBuilder = new StringBuilder();
        jobXmlBuilder.append("<flow-definition plugin=\"workflow-job@").append(WORKFLOW_JOB_PLUGIN_VERSION).append("\">\n")
                .append("  <description>CI/CD Pipeline for ").append(languageFramework).append(" application</description>\n")
                .append("  <keepDependencies>false</keepDependencies>\n")
                .append("  <properties/>\n")
                .append("  <definition class=\"org.jenkinsci.plugins.workflow.cps.CpsFlowDefinition\" plugin=\"workflow-cps@")
                .append(WORKFLOW_CPS_PLUGIN_VERSION).append("\">\n")
                .append("    <script>").append(escapeXml(jenkinsfileScript)).append("</script>\n")
                .append("    <sandbox>true</sandbox>\n")
                .append("  </definition>\n")
                .append("  <triggers/>\n")
                .append("  <disabled>false</disabled>\n")
                .append("</flow-definition>");

        jenkinsService.createOrUpdateJob(jobName, jobXmlBuilder.toString());
        System.out.println("Jenkins CI/CD Pipeline Job '" + jobName + "' created successfully for " + languageFramework + ".");
    }

    /**
     * Escapes XML special characters in Jenkinsfile script content.
     *
     * @param text The text to escape.
     * @return The escaped text.
     */
    private String escapeXml(String text) {
        if (text == null) return "";
        return text.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&apos;");
    }
}
