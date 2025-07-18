package com.infraxus.global.loki.service;

import com.infraxus.application.alarm.alert.service.AlertingService;
import com.infraxus.application.container.log.domain.ContainerLog;
import com.infraxus.application.container.log.domain.repository.ContainerLogRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LoggingService {

    private static final Logger logger = LoggerFactory.getLogger(LoggingService.class);
    private final ContainerLogRepository containerLogRepository;
    private final AlertingService alertingService;
    private final RestTemplate restTemplate = new RestTemplate();

    public void logEvent(String eventMessage) {
        logger.info("Application Event: {}", eventMessage);
    }

    public void fetchAndProcessLokiLogs(String query) {
        String lokiUrl = "http://loki:3100/loki/api/v1/query?query=" + query;
        try {
            String response = restTemplate.getForObject(lokiUrl, String.class);
            JSONObject jsonResponse = new JSONObject(response);
            JSONArray results = jsonResponse.getJSONObject("data").getJSONArray("result");

            for (int i = 0; i < results.length(); i++) {
                JSONObject result = results.getJSONObject(i);
                JSONArray values = result.getJSONArray("values");

                for (int j = 0; j < values.length(); j++) {
                    JSONArray logEntry = values.getJSONArray(j);
                    String logLine = logEntry.getString(1);
                    
                    // Simple parsing logic, assuming log line is a plain string for now
                    // This needs to be adapted based on the actual log format
                    ContainerLog containerLog = ContainerLog.builder()
                            .containerId(UUID.randomUUID()) // Or extract from log if available
                            .serverId(UUID.randomUUID()) // Or extract from log if available
                            .logType("Loki")
                            .logLevel("INFO") // Or extract from log
                            .logTitle("Loki Log")
                            .logContent(logLine)
                            .createAt(new Date(Long.parseLong(logEntry.getString(0)) / 1_000_000))
                            .build();
                    
                    containerLogRepository.save(containerLog);
                    alertingService.createAlertFromLog(containerLog);
                }
            }
        } catch (Exception e) {
            logger.error("Error fetching or processing logs from Loki", e);
        }
    }

    public void logContainerEvent(UUID serverId, UUID containerId, String logType, String logLevel, String logTitle, String logContent, String stackTrace) {
        ContainerLog containerLog = ContainerLog.builder()
                .containerId(containerId)
                .serverId(serverId)
                .logType(logType)
                .logLevel(logLevel)
                .logTitle(logTitle)
                .logContent(logContent)
                .stackTrace(stackTrace)
                .createAt(new Date())
                .build();
        containerLogRepository.save(containerLog);
        logger.info("Container Logged: ServerId={}, ContainerId={}, Type={}, Level={}, Title={}", serverId, containerId, logType, logLevel, logTitle);

        // Trigger alert creation based on the log
        alertingService.createAlertFromLog(containerLog);
    }

    @Scheduled(fixedRate = 60000) // 1분마다 실행
    public void processScheduledLogs() {
        logger.info("Processing scheduled logs...");
        fetchAndProcessLokiLogs("{job=\"container\"}");
    }
}
