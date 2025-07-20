package com.infraxus.application.alarm.alert.presentation;

import com.infraxus.application.alarm.alert.domain.Alert;
import com.infraxus.application.alarm.alert.service.CommandAlertService;
import com.infraxus.application.alarm.alert.service.QueryAlertService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/servers/{serverId}/containers/{containerId}/alerts")
@RequiredArgsConstructor
public class AlertController {

    private final CommandAlertService commandAlertService;
    private final QueryAlertService queryAlertService;

//    @PostMapping
//    public ResponseEntity<Void> createAlert(@PathVariable UUID serverId, @PathVariable UUID containerId, @RequestBody AlertCreateRequest request) {
//        // You might want to add serverId and containerId to the request or handle it in the service
//        commandAlertService.createAlert(request);
//        return ResponseEntity.ok().build();
//    }

    @GetMapping
    public ResponseEntity<List<Alert>> getAllAlertsByContainer(@PathVariable UUID serverId, @PathVariable UUID containerId) {
        return ResponseEntity.ok(queryAlertService.findAllByContainerId(containerId));
    }

    @GetMapping("/{alertId}")
    public ResponseEntity<Alert> getAlertById(@PathVariable UUID serverId, @PathVariable UUID containerId, @PathVariable UUID alertId) {
        return ResponseEntity.ok(queryAlertService.findById(alertId));
    }

//    @PutMapping("/{alertId}")
//    public ResponseEntity<Void> updateAlert(@PathVariable UUID serverId, @PathVariable UUID containerId, @PathVariable UUID alertId, @RequestBody AlertUpdateRequest request) {
//        commandAlertService.updateAlert(alertId, request);
//        return ResponseEntity.ok().build();
//    }

//    @DeleteMapping("/{alertId}")
//    public ResponseEntity<Void> deleteAlert(@PathVariable UUID serverId, @PathVariable UUID containerId, @PathVariable UUID alertId) {
//        commandAlertService.deleteAlert(alertId);
//        return ResponseEntity.ok().build();
//    }
}
