package com.infraxus.application.monitoring.presentation;

import com.infraxus.application.monitoring.presentation.dto.MetricResponse;
import com.infraxus.application.monitoring.service.MonitoringService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/monitoring")
@RequiredArgsConstructor
public class MonitoringController {

    private final MonitoringService monitoringService;

//    @GetMapping(value = "/metrics/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
//    public SseEmitter streamMetrics() {
//        return monitoringService.addEmitter();
//    }

    @GetMapping("/containers/{containerId}/metrics")
    public ResponseEntity<MetricResponse> getContainerMetrics(@PathVariable UUID containerId) {
        try {
            MetricResponse metrics = monitoringService.getContainerMetrics(containerId);
            return ResponseEntity.ok(metrics);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null); // Or a more specific error response
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null); // Or a more specific error response
        }
    }

    @GetMapping("/servers/{serverId}/metrics")
    public ResponseEntity<MetricResponse> getServerMetrics(@PathVariable UUID serverId) {
        try {
            MetricResponse metrics = monitoringService.getServerMetrics(serverId);
            return ResponseEntity.ok(metrics);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null); // Or a more specific error response
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null); // Or a more specific error response
        }
    }
}
