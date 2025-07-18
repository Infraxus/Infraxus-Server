package com.infraxus.application.container.log.service.implementation;

import com.infraxus.application.container.log.domain.ContainerLog;
import com.infraxus.application.container.log.domain.repository.ContainerLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ContainerLogUpdater {

    private final ContainerLogRepository containerLogRepository;

    public void update(ContainerLog updatableContainerLog, ContainerLog newContainerLogData){
        ContainerLog updated = updatableContainerLog.toBuilder()
                .containerId(updatableContainerLog.getContainerId())
                .serverId(newContainerLogData.getServerId())
                .logType(newContainerLogData.getLogType())
                .logLevel(newContainerLogData.getLogLevel())
                .logTitle(newContainerLogData.getLogTitle())
                .logContent(newContainerLogData.getLogContent())
                .stackTrace(newContainerLogData.getStackTrace())
                .build();

        containerLogRepository.save(updated);
    }

}
