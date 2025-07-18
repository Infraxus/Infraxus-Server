package com.infraxus.application.alarm.alarm.presentation;

import com.infraxus.application.alarm.alarm.domain.Alarm;
import com.infraxus.application.alarm.alarm.domain.value.AlarmId;
import com.infraxus.application.alarm.alarm.service.QueryAlarmService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/alarms")
@RequiredArgsConstructor
public class AlarmController {

    private final QueryAlarmService queryAlarmService;

    @GetMapping
    public ResponseEntity<List<Alarm>> getAllAlarms() {
        return ResponseEntity.ok(queryAlarmService.findAll());
    }

    @GetMapping("/{serverId}/{containerId}")
    public ResponseEntity<Alarm> getAlarmById(@PathVariable UUID serverId, @PathVariable UUID containerId) {
        AlarmId alarmId = AlarmId.builder()
                .containerId(containerId)
                .serverId(serverId)
                .build();
        return ResponseEntity.ok(queryAlarmService.findById(alarmId));
    }
}
