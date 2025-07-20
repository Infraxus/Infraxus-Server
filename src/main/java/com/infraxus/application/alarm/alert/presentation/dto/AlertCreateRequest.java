package com.infraxus.application.alarm.alert.presentation.dto;

import com.infraxus.application.alarm.alert.domain.Alert;
import com.infraxus.application.alarm.alert.domain.value.AlertKey;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Getter
@NoArgsConstructor
public class AlertCreateRequest {
    private UUID containerId;
    private UUID serverId;
    private String alertType;
    private String alertTitle;
    private String alertDescription;

    @Builder
    public AlertCreateRequest(UUID containerId, UUID serverId, String alertType, String alertTitle, String alertDescription) {
        this.containerId = containerId;
        this.serverId = serverId;
        this.alertType = alertType;
        this.alertTitle = alertTitle;
        this.alertDescription = alertDescription;
    }

    public Alert toEntity() {
        return Alert.builder()
                .alertKey(new AlertKey(
                        UUID.randomUUID(),
                        this.containerId,
                        this.serverId
                ))
                .alertType(this.alertType)
                .alertTitle(this.alertTitle)
                .alertDescription(this.alertDescription)
                .createAt(new Date())
                .build();
    }
}
