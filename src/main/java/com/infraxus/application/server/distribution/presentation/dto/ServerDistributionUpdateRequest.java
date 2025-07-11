package com.infraxus.application.server.distribution.presentation.dto;

import com.infraxus.application.server.distribution.domain.ServerDistribution;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ServerDistributionUpdateRequest {
    private Integer deploymentUnit;
    private Integer replacementWaitingTime;
    private Integer maxUnavailable;
    private Integer maxSurge;
    private Integer initialTrafficRatio;
    private Integer expansionRate;
    private Integer expansionCycle;
    private Integer autoStopThreshold;
    private String healthCheckPath;
    private Integer healthCheckCycle;
    private Integer maxFailuresAllowed;
    private Integer rollbackThreshold;

    @Builder
    public ServerDistributionUpdateRequest(Integer deploymentUnit, Integer replacementWaitingTime, Integer maxUnavailable, Integer maxSurge, Integer initialTrafficRatio, Integer expansionRate, Integer expansionCycle, Integer autoStopThreshold, String healthCheckPath, Integer healthCheckCycle, Integer maxFailuresAllowed, Integer rollbackThreshold) {
        this.deploymentUnit = deploymentUnit;
        this.replacementWaitingTime = replacementWaitingTime;
        this.maxUnavailable = maxUnavailable;
        this.maxSurge = maxSurge;
        this.initialTrafficRatio = initialTrafficRatio;
        this.expansionRate = expansionRate;
        this.expansionCycle = expansionCycle;
        this.autoStopThreshold = autoStopThreshold;
        this.healthCheckPath = healthCheckPath;
        this.healthCheckCycle = healthCheckCycle;
        this.maxFailuresAllowed = maxFailuresAllowed;
        this.rollbackThreshold = rollbackThreshold;
    }

    public ServerDistribution toEntity() {
        return ServerDistribution.builder()
                .deploymentUnit(this.deploymentUnit)
                .replacementWaitingTime(this.replacementWaitingTime)
                .maxUnavailable(this.maxUnavailable)
                .maxSurge(this.maxSurge)
                .initialTrafficRatio(this.initialTrafficRatio)
                .expansionRate(this.expansionRate)
                .expansionCycle(this.expansionCycle)
                .autoStopThreshold(this.autoStopThreshold)
                .healthCheckPath(this.healthCheckPath)
                .healthCheckCycle(this.healthCheckCycle)
                .maxFailuresAllowed(this.maxFailuresAllowed)
                .rollbackThreshold(this.rollbackThreshold)
                .build();
    }
}
