package com.infraxus.global.jenkins;

import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Service
public class JenkinsTemplateService {

    private static final String TEMPLATE_PATH_PREFIX = "/jenkins-files/";

    public String createScriptFromTemplate(String languageFramework, Map<String, String> parameters) throws IOException {
        String baseScript = getBaseTemplate();
        String buildSteps = getBuildStepsTemplate(languageFramework);

        // Inject build steps into the base template
        String finalScript = baseScript.replace("// BUILD_STEPS_HERE", buildSteps);

        // Replace parameters
        for (Map.Entry<String, String> entry : parameters.entrySet()) {
            finalScript = finalScript.replace("${_" + entry.getKey() + "_}", entry.getValue());
        }

        // Wrap the final script in the required XML structure for a Jenkins pipeline job
        return toJenkinsJobXml(finalScript);
    }

    private String toJenkinsJobXml(String script) {
        String escapedScript = script.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
        return "<org.jenkinsci.plugins.workflow.job.WorkflowJob plugin=\"workflow-job@2.40\">\n" +
               "  <definition class=\"org.jenkinsci.plugins.workflow.cps.CpsFlowDefinition\" plugin=\"workflow-cps@2.92\">\n" +
               "    <script>" + escapedScript + "</script>\n" +
               "    <sandbox>true</sandbox>\n" +
               "  </definition>\n" +
               "  <disabled>false</disabled>\n" +
               "</org.jenkinsci.plugins.workflow.job.WorkflowJob>";
    }


    private String getBaseTemplate() throws IOException {
        String templatePath = TEMPLATE_PATH_PREFIX + "base.groovy";
        try (InputStream inputStream = getClass().getResourceAsStream(templatePath)) {
            if (inputStream == null) {
                throw new IOException("Base template not found at: " + templatePath);
            }
            return FileCopyUtils.copyToString(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
        }
    }

    private String getBuildStepsTemplate(String languageFramework) throws IOException {
        String templatePath = getTemplatePath(languageFramework);
        try (InputStream inputStream = getClass().getResourceAsStream(templatePath)) {
            if (inputStream == null) {
                throw new IOException("Build steps template not found for: " + languageFramework);
            }
            return FileCopyUtils.copyToString(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
        }
    }

    private String getTemplatePath(String languageFramework) {
        // This logic can be expanded based on your template organization
        switch (languageFramework.toLowerCase()) {
            case "spring":
                return TEMPLATE_PATH_PREFIX + "java/springboot.groovy";
            case "django":
                return TEMPLATE_PATH_PREFIX + "python/django.groovy";
            case "fastapi":
                return TEMPLATE_PATH_PREFIX + "python/fastapi.groovy";
            case "nest":
                return TEMPLATE_PATH_PREFIX + "javascript/nest.groovy";
            case "express":
                return TEMPLATE_PATH_PREFIX + "javascript/express.groovy";
            default:
                throw new IllegalArgumentException("Unsupported language/framework: " + languageFramework);
        }
    }
}
