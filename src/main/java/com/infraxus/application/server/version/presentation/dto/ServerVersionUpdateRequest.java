package com.infraxus.application.server.version.presentation.dto;

import com.infraxus.application.server.version.domain.ServerVersion;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ServerVersionUpdateRequest {
    private Integer switchingTiming;
    private Integer monitoringCriteria;
    private Integer trafficSplitRatio;
    private Integer switchingMethod;

    @Builder
    public ServerVersionUpdateRequest(Integer switchingTiming, Integer monitoringCriteria, Integer trafficSplitRatio, Integer switchingMethod) {
        this.switchingTiming = switchingTiming;
        this.monitoringCriteria = monitoringCriteria;
        this.trafficSplitRatio = trafficSplitRatio;
        this.switchingMethod = switchingMethod;
    }

    public ServerVersion toEntity() {
        return ServerVersion.builder()
                .switchingTiming(this.switchingTiming)
                .monitoringCriteria(this.monitoringCriteria)
                .trafficSplitRatio(this.trafficSplitRatio)
                .switchingMethod(this.switchingMethod)
                .build();
    }
}
