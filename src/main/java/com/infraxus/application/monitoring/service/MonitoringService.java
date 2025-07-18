package com.infraxus.application.monitoring.service;

import com.infraxus.application.alarm.alert.service.AlertingService;
import com.infraxus.application.container.container.domain.Container;
import com.infraxus.application.container.container.domain.repository.ContainerRepository;
import com.infraxus.application.monitoring.presentation.dto.MetricResponse;
import com.infraxus.global.docker.DockerService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
@RequiredArgsConstructor
public class MonitoringService {

    private static final Logger logger = LoggerFactory.getLogger(MonitoringService.class);

    @Value("${prometheus.url}")
    private String prometheusUrl;
    private final RestTemplate restTemplate;
    private final CopyOnWriteArrayList<SseEmitter> emitters = new CopyOnWriteArrayList<>();
    private final DockerService dockerService;
    private final AlertingService alertingService;
    private final ContainerRepository containerRepository;

    public SseEmitter addEmitter() {
        SseEmitter emitter = new SseEmitter(600_000L);
        emitters.add(emitter);

        emitter.onCompletion(() -> emitters.remove(emitter));
        emitter.onTimeout(() -> emitters.remove(emitter));

        return emitter;
    }

    @Scheduled(fixedRate = 5000)
    public void sendMetrics() {
        if (emitters.isEmpty()) {
            return;
        }

        List<Container> allContainers = containerRepository.findAll();

        double totalCpuUsage = 0.0;
        double totalMemoryUsageBytes = 0.0;
        long totalMemoryTotal = 0L;
        double totalNetworkRxBytes = 0.0;
        double totalNetworkTxBytes = 0.0;
        double totalDiskReadBytes = 0.0;
        double totalDiskWriteBytes = 0.0;

        for (Container container : allContainers) {
            try {
                MetricResponse containerMetrics = getContainerMetrics(container.getContainerId());
                totalCpuUsage += containerMetrics.getCpuUsage();
                totalMemoryUsageBytes += containerMetrics.getMemoryUsage();
                totalMemoryTotal += containerMetrics.getMemoryTotal();
                totalNetworkRxBytes += containerMetrics.getNetworkRxBytes();
                totalNetworkTxBytes += containerMetrics.getNetworkTxBytes();
                totalDiskReadBytes += containerMetrics.getDiskReadBytes();
                totalDiskWriteBytes += containerMetrics.getDiskWriteBytes();
            } catch (Exception e) {
                logger.warn("Could not retrieve metrics for container {}: {}", container.getContainerId(), e.getMessage());
            }
        }

        long totalMemoryFree = totalMemoryTotal - (long)totalMemoryUsageBytes;
        if (totalMemoryFree < 0) totalMemoryFree = 0;

        MetricResponse aggregatedMetrics = MetricResponse.builder()
                .cpuUsage(totalCpuUsage)
                .memoryUsage(totalMemoryUsageBytes)
                .memoryTotal(totalMemoryTotal)
                .memoryFree(totalMemoryFree)
                .networkRxBytes(totalNetworkRxBytes)
                .networkTxBytes(totalNetworkTxBytes)
                .diskReadBytes(totalDiskReadBytes)
                .diskWriteBytes(totalDiskWriteBytes)
                .build();

        for (SseEmitter emitter : emitters) {
            try {
                emitter.send(SseEmitter.event().name("metrics").data(aggregatedMetrics));
            } catch (IOException e) {
                emitters.remove(emitter);
            }
        }
    }

    public void checkDockerContainerStatus() {
        logger.info("Checking Docker container status...");
        String containerListOutput = dockerService.listAllContainers();
        List<String> containers = Arrays.asList(containerListOutput.split(""));

        for (String containerInfo : containers) {
            if (containerInfo.trim().isEmpty()) continue;

            String[] parts = containerInfo.split("	");
            if (parts.length < 4) {
                logger.warn("Unexpected container info format: {}", containerInfo);
                continue;
            }
            String containerId = parts[0];
            String containerName = parts[1];
            String containerStatus = parts[3];

            if (containerStatus.contains("Exited")) {
                String alertMessage = String.format("Docker container '%s' (%s) is in 'Exited' state. Please investigate.", containerName, containerId);
                logger.warn(alertMessage);
                alertingService.sendAlert(alertMessage);
            }
        }
        logger.info("Docker container status check completed.");
    }

    public MetricResponse getContainerMetrics(UUID containerId) {
        Container container = containerRepository.findById(containerId)
                .orElseThrow(() -> new IllegalArgumentException("Container not found with ID: " + containerId));

        String containerName = container.getContainerName();

        String cpuQuery = String.format("%s/api/v1/query?query=sum(rate(container_cpu_usage_seconds_total{name=\"%s\"}[1m]))", prometheusUrl, containerName);
        double cpuUsage = parsePrometheusQueryResult(restTemplate.getForObject(cpuQuery, String.class));

        String memoryUsageQuery = String.format("%s/api/v1/query?query=container_memory_usage_bytes{name=\"%s\"}", prometheusUrl, containerName);
        double memoryUsageBytes = parsePrometheusQueryResult(restTemplate.getForObject(memoryUsageQuery, String.class));

        String memoryTotalQuery = String.format("%s/api/v1/query?query=container_spec_memory_limit_bytes{name=\"%s\"}", prometheusUrl, containerName);
        long memoryTotal = (long) parsePrometheusQueryResult(restTemplate.getForObject(memoryTotalQuery, String.class));
        if (memoryTotal == 0) {
            memoryTotal = 16 * 1024 * 1024 * 1024L; // Default to 16GB
        }
        long memoryFree = memoryTotal - (long)memoryUsageBytes;
        if (memoryFree < 0) memoryFree = 0;

        String networkRxQuery = String.format("%s/api/v1/query?query=sum(rate(container_network_receive_bytes_total{name=\"%s\"}[1m]))", prometheusUrl, containerName);
        double networkRxBytes = parsePrometheusQueryResult(restTemplate.getForObject(networkRxQuery, String.class));

        String networkTxQuery = String.format("%s/api/v1/query?query=sum(rate(container_network_transmit_bytes_total{name=\"%s\"}[1m]))", prometheusUrl, containerName);
        double networkTxBytes = parsePrometheusQueryResult(restTemplate.getForObject(networkTxQuery, String.class));

        String diskReadQuery = String.format("%s/api/v1/query?query=sum(rate(container_fs_reads_bytes_total{name=\"%s\"}[1m]))", prometheusUrl, containerName);
        double diskReadBytes = parsePrometheusQueryResult(restTemplate.getForObject(diskReadQuery, String.class));

        String diskWriteQuery = String.format("%s/api/v1/query?query=sum(rate(container_fs_writes_bytes_total{name=\"%s\"}[1m]))", prometheusUrl, containerName);
        double diskWriteBytes = parsePrometheusQueryResult(restTemplate.getForObject(diskWriteQuery, String.class));

        return MetricResponse.builder()
                .cpuUsage(cpuUsage)
                .memoryUsage(memoryUsageBytes)
                .memoryTotal(memoryTotal)
                .memoryFree(memoryFree)
                .networkRxBytes(networkRxBytes)
                .networkTxBytes(networkTxBytes)
                .diskReadBytes(diskReadBytes)
                .diskWriteBytes(diskWriteBytes)
                .build();
    }

    public MetricResponse getServerMetrics(UUID serverId) {
        List<Container> containers = containerRepository.findAllByServerId(serverId);
        if (containers.isEmpty()) {
            throw new IllegalArgumentException("No containers found for server ID: " + serverId);
        }

        double totalCpuUsage = 0.0;
        double totalMemoryUsageBytes = 0.0;
        long totalMemoryTotal = 0L;
        double totalNetworkRxBytes = 0.0;
        double totalNetworkTxBytes = 0.0;
        double totalDiskReadBytes = 0.0;
        double totalDiskWriteBytes = 0.0;

        for (Container container : containers) {
            MetricResponse containerMetrics = getContainerMetrics(container.getContainerId());
            totalCpuUsage += containerMetrics.getCpuUsage();
            totalMemoryUsageBytes += containerMetrics.getMemoryUsage();
            totalMemoryTotal += containerMetrics.getMemoryTotal();
            totalNetworkRxBytes += containerMetrics.getNetworkRxBytes();
            totalNetworkTxBytes += containerMetrics.getNetworkTxBytes();
            totalDiskReadBytes += containerMetrics.getDiskReadBytes();
            totalDiskWriteBytes += containerMetrics.getDiskWriteBytes();
        }

        long totalMemoryFree = totalMemoryTotal - (long)totalMemoryUsageBytes;
        if (totalMemoryFree < 0) totalMemoryFree = 0;

        return MetricResponse.builder()
                .cpuUsage(totalCpuUsage)
                .memoryUsage(totalMemoryUsageBytes)
                .memoryTotal(totalMemoryTotal)
                .memoryFree(totalMemoryFree)
                .networkRxBytes(totalNetworkRxBytes)
                .networkTxBytes(totalNetworkTxBytes)
                .diskReadBytes(totalDiskReadBytes)
                .diskWriteBytes(totalDiskWriteBytes)
                .build();
    }

    private double parsePrometheusQueryResult(String jsonResult) {
        try {
            JSONObject jsonObject = new JSONObject(jsonResult);
            String status = jsonObject.getString("status");
            if ("success".equals(status)) {
                JSONObject data = jsonObject.getJSONObject("data");
                JSONArray result = data.getJSONArray("result");
                if (result.length() > 0) {
                    JSONObject firstResult = result.getJSONObject(0);
                    JSONArray value = firstResult.getJSONArray("value");
                    return Double.parseDouble(value.getString(1));
                }
            }
        } catch (Exception e) {
            logger.error("Error parsing Prometheus query result: {}", jsonResult, e);
        }
        return 0.0;
    }
}
