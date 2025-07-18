package com.infraxus.application.monitoring.service;

import com.infraxus.application.monitoring.presentation.dto.MetricResponse;
import com.infraxus.application.server.resources.domain.ServerResources;
import com.infraxus.application.server.resources.domain.repository.ServerResourcesRepository;
import com.infraxus.application.server.server.domain.Server;
import com.infraxus.application.server.server.domain.repository.ServerRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import lombok.RequiredArgsConstructor;
import com.infraxus.global.loki.service.LoggingService;
import com.infraxus.application.alarm.alert.service.AlertingService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SystemHealthCheckService {

    private static final Logger logger = LoggerFactory.getLogger(SystemHealthCheckService.class);

    private final LoggingService loggingService;
    private final AlertingService alertingService;
    private final MonitoringService monitoringService;
    private final ServerRepository serverRepository;
    private final ServerResourcesRepository serverResourcesRepository;

    @Value("${infraxus.scheduling.healthCheckIntervalMs:60000}")
    private long healthCheckIntervalMs;

    @Value("${infraxus.monitoring.cpu.warning.threshold:0.7}")
    private double cpuWarningThreshold;

    @Value("${infraxus.monitoring.cpu.critical.threshold:0.95}")
    private double cpuCriticalThreshold;

    @Value("${infraxus.monitoring.memory.warning.threshold:0.7}")
    private double memoryWarningThreshold;

    @Value("${infraxus.monitoring.memory.critical.threshold:0.95}")
    private double memoryCriticalThreshold;

    @Scheduled(fixedRateString = "${infraxus.scheduling.healthCheckIntervalMs:60000}")
    public void performSystemHealthCheck() {
        logger.info("Performing scheduled system health check...");
        monitoringService.sendMetrics();
        monitoringService.checkDockerContainerStatus();
        checkServerResourceUsage();
        logger.info("System health check completed.");
    }

    public void checkServerResourceUsage() {
        List<Server> servers = serverRepository.findAll();
        for (Server server : servers) {
            serverResourcesRepository.findById(server.getServerId()).ifPresent(resources -> {
                try {
                    MetricResponse serverMetrics = monitoringService.getServerMetrics(server.getServerId());

                    // Check CPU Usage
                    if (resources.getCpuResources() > 0) {
                        double currentCpuUsage = serverMetrics.getCpuUsage() / resources.getCpuResources();
                        checkAndAlert("CPU", currentCpuUsage, cpuWarningThreshold, cpuCriticalThreshold, server.getServerName());
                    }

                    // Check Memory Usage (assuming memoryResources is in GB, convert to Bytes)
                    if (resources.getMemoryResources() > 0) {
                        long totalMemoryBytes = resources.getMemoryResources() * 1024L * 1024L * 1024L;
                        double currentMemoryUsage = serverMetrics.getMemoryUsage() / totalMemoryBytes;
                        checkAndAlert("Memory", currentMemoryUsage, memoryWarningThreshold, memoryCriticalThreshold, server.getServerName());
                    }
                } catch (Exception e) {
                    logger.error("Failed to check resource usage for server {}", server.getServerName(), e);
                }
            });
        }
    }

    private void checkAndAlert(String resourceType, double currentUsage, double warningThreshold, double criticalThreshold, String serverName) {
        String alertLevel = null;
        if (currentUsage > criticalThreshold) {
            alertLevel = "Critical";
        } else if (currentUsage > warningThreshold) {
            alertLevel = "Warning";
        }

        if (alertLevel != null) {
            String message = String.format("[%s] High %s usage detected on server %s. Usage: %.2f%%",
                    alertLevel, resourceType, serverName, currentUsage * 100);
            alertingService.sendAlert(message);
            logger.warn(message);
        }
    }

    @Scheduled(fixedRateString = "${infraxus.scheduling.loggingIntervalMs:300000}")
    public void performLoggingTasks() {
        logger.info("Performing scheduled logging tasks...");
        loggingService.processScheduledLogs();
        logger.info("Logging tasks completed.");
    }

    @Scheduled(fixedRateString = "${infraxus.scheduling.alertingIntervalMs:120000}")
    public void performAlertingTasks() {
        logger.info("Performing scheduled alerting tasks...");
        alertingService.checkAndSendAlerts();
        logger.info("Alerting tasks completed.");
    }
}
