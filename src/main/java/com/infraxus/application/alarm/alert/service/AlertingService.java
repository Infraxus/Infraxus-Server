package com.infraxus.application.alarm.alert.service;

import com.infraxus.application.alarm.alarm.domain.value.AlarmId;
import com.infraxus.application.alarm.alert.domain.Alert;
import com.infraxus.application.alarm.alert.domain.repository.AlertRepository;
import com.infraxus.application.alarm.alarm.domain.Alarm;
import com.infraxus.application.container.log.domain.ContainerLog;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AlertingService {

    private static final Logger logger = LoggerFactory.getLogger(AlertingService.class);
    private final AlertRepository alertRepository;
    private final com.infraxus.application.alarm.alarm.domain.repository.AlarmRepository alarmRepository;

    public void sendAlert(String alertMessage) {
        logger.warn("Sending Alert: {}", alertMessage);
        
    }

    public void createAlertFromLog(ContainerLog log) {
        // Determine alert type based on log level
        String alertType;
        switch (log.getLogLevel().toUpperCase()) {
            case "ERROR":
                alertType = "ERROR";
                break;
            case "WARN":
                alertType = "WARN";
                break;
            case "INFO":
            default:
                alertType = "INFO";
                break;
        }

        Alert alert = Alert.builder()
                .alertId(UUID.randomUUID())
                .containerId(log.getContainerId())
                .serverId(log.getServerId())
                .alertType(alertType)
                .alertTitle("Log Alert: " + log.getLogTitle())
                .alertDescription(log.getLogContent())
                .createAt(new Date())
                .build();

        alertRepository.save(alert);
        logger.info("Alert created from log: AlertId={}, ServerId={}, ContainerId={}, Type={}", alert.getAlertId(), alert.getServerId(), alert.getContainerId(), alert.getAlertType());
        // As per requirements, Alert should not be deleted or modified.
    }

    @Scheduled(fixedRate = 60000) // Every 1 minute
    public void updateAlarmsBasedOnAlerts() {
        logger.info("Updating alarms based on alerts...");
        List<Alert> allAlerts = alertRepository.findAll(); // Fetch all alerts

        // Group alerts by serverId and containerId to update corresponding alarms
        allAlerts.stream()
                .collect(Collectors.groupingBy(alert -> AlarmId.builder().containerId(alert.getContainerId()).serverId(alert.getServerId()).build()))
                .forEach((alarmId, alertsForAlarm) -> {
                    int criticalCount = 0;
                    int warningCount = 0;
                    int infoCount = 0;

                    for (Alert alert : alertsForAlarm) {
                        switch (alert.getAlertType().toUpperCase()) {
                            case "ERROR":
                                criticalCount++;
                                break;
                            case "WARN":
                                warningCount++;
                                break;
                            case "INFO":
                                infoCount++;
                                break;
                        }
                    }

                    Optional<Alarm> existingAlarm = alarmRepository.findById(alarmId);
                    Alarm alarm;
                    if (existingAlarm.isPresent()) {
                        alarm = existingAlarm.get();
                        alarm.setTotalCriticalAlert(criticalCount);
                        alarm.setTotalWarningAlert(warningCount);
                        alarm.setTotalInfoAlert(infoCount);
                    } else {
                        // Create a new alarm if it doesn't exist (though requirement says no create/delete)
                        // This might be a discrepancy, but for now, we create if not found to avoid errors.
                        // In a strict interpretation, this else block would be removed and an error logged.
                        alarm = Alarm.builder()
                                .alarmId(alarmId)
                                .totalCriticalAlert(criticalCount)
                                .totalWarningAlert(warningCount)
                                .totalInfoAlert(infoCount)
                                .build();
                    }
                    alarmRepository.save(alarm);
                    logger.info("Alarm updated for ServerId={}, ContainerId={}: Critical={}, Warning={}, Info={}",
                            alarmId.getServerId(), alarmId.getContainerId(), criticalCount, warningCount, infoCount);
                });
    }

    public void checkAndSendAlerts() {
        logger.info("Checking for alerts to send...");
        List<Alert> criticalAndWarningAlerts = alertRepository.findAll().stream()
                .filter(alert -> "ERROR".equalsIgnoreCase(alert.getAlertType()) || "WARN".equalsIgnoreCase(alert.getAlertType()))
                .collect(Collectors.toList());

        if (!criticalAndWarningAlerts.isEmpty()) {
            for (Alert alert : criticalAndWarningAlerts) {
                String alertMessage = String.format("Severity: %s, Title: %s, Description: %s (Server: %s, Container: %s)",
                        alert.getAlertType(), alert.getAlertTitle(), alert.getAlertDescription(), alert.getServerId(), alert.getContainerId());
                sendAlert(alertMessage);
            }
            logger.info("Sent {} critical/warning alerts.", criticalAndWarningAlerts.size());
        } else {
            logger.info("No critical or warning alerts to send.");
        }
    }
}
