package com.infraxus.application.server.version.presentation.dto;

import com.infraxus.application.server.version.domain.ServerVersion;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
public class ServerVersionCreateRequest {
    private Integer switchingTiming;
    private Integer monitoringCriteria;
    private Integer trafficSplitRatio;
    private Integer switchingMethod;

    @Builder
    public ServerVersionCreateRequest(Integer switchingTiming, Integer monitoringCriteria, Integer trafficSplitRatio, Integer switchingMethod) {
        this.switchingTiming = switchingTiming;
        this.monitoringCriteria = monitoringCriteria;
        this.trafficSplitRatio = trafficSplitRatio;
        this.switchingMethod = switchingMethod;
    }

    public ServerVersion toEntity() {
        return ServerVersion.builder()
                .serverId(UUID.randomUUID())
                .switchingTiming(this.switchingTiming)
                .monitoringCriteria(this.monitoringCriteria)
                .trafficSplitRatio(this.trafficSplitRatio)
                .switchingMethod(this.switchingMethod)
                .build();
    }
}
