package com.infraxus.application.server.version.service.implementation;

import com.infraxus.application.server.version.domain.ServerVersion;
import com.infraxus.application.server.version.domain.repository.ServerVersionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ServerVersionUpdater {

    private final ServerVersionRepository serverVersionRepository;

    public void update(ServerVersion updatableServerVersion, ServerVersion newServerVersionData){
        updatableServerVersion.toBuilder()
                .switchingTiming(newServerVersionData.getSwitchingTiming())
                .monitoringCriteria(newServerVersionData.getMonitoringCriteria())
                .trafficSplitRatio(newServerVersionData.getTrafficSplitRatio())
                .switchingMethod(newServerVersionData.getSwitchingMethod())
                .build();

        serverVersionRepository.save(updatableServerVersion);
    }

}
