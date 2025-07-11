package com.infraxus.application.alram.alram.presentation.dto;

import com.infraxus.application.alram.alram.domain.Alarm;
import com.infraxus.application.alram.alram.domain.value.AlarmId;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
public class AlarmCreateRequest {
    private UUID serverId;
    private UUID containerId;
    private String alarmType;
    private Integer totalCriticalAlert;
    private Integer totalWarningAlert;
    private Integer totalInfoAlert;

    @Builder
    public AlarmCreateRequest(UUID serverId, UUID containerId, String alarmType, Integer totalCriticalAlert, Integer totalWarningAlert, Integer totalInfoAlert) {
        this.serverId = serverId;
        this.containerId = containerId;
        this.alarmType = alarmType;
        this.totalCriticalAlert = totalCriticalAlert;
        this.totalWarningAlert = totalWarningAlert;
        this.totalInfoAlert = totalInfoAlert;
    }

    public Alarm toEntity() {
        AlarmId alarmId = AlarmId.builder()
                .serverId(this.containerId)
                .containerId(this.containerId)
                .build();
        return Alarm.builder()
                .alarmId(alarmId)
                .totalCriticalAlert(this.totalCriticalAlert)
                .totalWarningAlert(this.totalWarningAlert)
                .totalInfoAlert(this.totalInfoAlert)
                .build();
    }
}
