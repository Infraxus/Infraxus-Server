package com.infraxus.application.alram.alert.presentation;

import com.infraxus.application.alram.alert.domain.Alert;
import com.infraxus.application.alram.alert.presentation.dto.AlertCreateRequest;
import com.infraxus.application.alram.alert.presentation.dto.AlertUpdateRequest;
import com.infraxus.application.alram.alert.service.CommandAlertService;
import com.infraxus.application.alram.alert.service.QueryAlertService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/alerts")
@RequiredArgsConstructor
public class AlertController {

    private final QueryAlertService queryAlertService;

    @GetMapping
    public ResponseEntity<List<Alert>> getAllAlerts() {
        return ResponseEntity.ok(queryAlertService.findAll());
    }

    @GetMapping("/{alertId}")
    public ResponseEntity<Alert> getAlertById(@PathVariable UUID alertId) {
        return ResponseEntity.ok(queryAlertService.findById(alertId));
    }
}
