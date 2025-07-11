package com.infraxus.application.alram.alram.presentation.dto;

import com.infraxus.application.alram.alram.domain.Alarm;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AlarmUpdateRequest {
    private Integer totalCriticalAlert;
    private Integer totalWarningAlert;
    private Integer totalInfoAlert;

    @Builder
    public AlarmUpdateRequest(Integer totalCriticalAlert, Integer totalWarningAlert, Integer totalInfoAlert) {
        this.totalCriticalAlert = totalCriticalAlert;
        this.totalWarningAlert = totalWarningAlert;
        this.totalInfoAlert = totalInfoAlert;
    }

    public Alarm toEntity() {
        return Alarm.builder()
                .totalCriticalAlert(this.totalCriticalAlert)
                .totalWarningAlert(this.totalWarningAlert)
                .totalInfoAlert(this.totalInfoAlert)
                .build();
    }
}
