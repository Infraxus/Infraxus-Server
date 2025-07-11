package com.infraxus.application.server.distribution.service.implementation;

import com.infraxus.application.server.distribution.domain.ServerDistribution;
import com.infraxus.application.server.distribution.domain.repository.ServerDistributionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ServerDistributionUpdater {

    private final ServerDistributionRepository serverDistributionRepository;

    public void update(ServerDistribution updatableServerDistribution, ServerDistribution newServerDistributionData){
        updatableServerDistribution.toBuilder()
                .deploymentUnit(newServerDistributionData.getDeploymentUnit())
                .replacementWaitingTime(newServerDistributionData.getReplacementWaitingTime())
                .maxUnavailable(newServerDistributionData.getMaxUnavailable())
                .maxSurge(newServerDistributionData.getMaxSurge())
                .initialTrafficRatio(newServerDistributionData.getInitialTrafficRatio())
                .expansionRate(newServerDistributionData.getExpansionRate())
                .expansionCycle(newServerDistributionData.getExpansionCycle())
                .autoStopThreshold(newServerDistributionData.getAutoStopThreshold())
                .healthCheckPath(newServerDistributionData.getHealthCheckPath())
                .healthCheckCycle(newServerDistributionData.getHealthCheckCycle())
                .maxFailuresAllowed(newServerDistributionData.getMaxFailuresAllowed())
                .rollbackThreshold(newServerDistributionData.getRollbackThreshold())
                .build();

        serverDistributionRepository.save(updatableServerDistribution);
    }

}
