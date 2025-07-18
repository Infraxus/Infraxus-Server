package com.infraxus.application.alarm.alert.presentation.dto;

import com.infraxus.application.alarm.alert.domain.Alert;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
public class AlertUpdateRequest {
    private UUID containerId;
    private UUID serverId;
    private String alertType;
    private String alertTitle;
    private String alertDescription;

    @Builder
    public AlertUpdateRequest(UUID containerId, UUID serverId, String alertType, String alertTitle, String alertDescription) {
        this.containerId = containerId;
        this.serverId = serverId;
        this.alertType = alertType;
        this.alertTitle = alertTitle;
        this.alertDescription = alertDescription;
    }

    public Alert toEntity() {
        return Alert.builder()
                .containerId(this.containerId)
                .serverId(this.serverId)
                .alertType(this.alertType)
                .alertTitle(this.alertTitle)
                .alertDescription(this.alertDescription)
                .build();
    }
}
