package com.infraxus.application.container.log.service;

import com.infraxus.application.container.log.domain.ContainerLog;
import com.infraxus.application.container.log.presentation.dto.ContainerLogCreateRequest;
import com.infraxus.application.container.log.presentation.dto.ContainerLogUpdateRequest;
import com.infraxus.application.container.log.service.implementation.ContainerLogCreator;
import com.infraxus.application.container.log.service.implementation.ContainerLogDeleter;
import com.infraxus.application.container.log.service.implementation.ContainerLogReader;
import com.infraxus.application.container.log.service.implementation.ContainerLogUpdater;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CommandContainerLogService {

    private final ContainerLogCreator containerLogCreator;
    private final ContainerLogUpdater containerLogUpdater;
    private final ContainerLogDeleter containerLogDeleter;
    private final ContainerLogReader containerLogReader;
    private final QueryContainerLogService queryContainerLogService;

    public void createContainerLog(ContainerLogCreateRequest request){
        containerLogCreator.save(request.toEntity());
    }

    public void updateContainerLog(UUID containerId, ContainerLogUpdateRequest request){
        ContainerLog containerLog = queryContainerLogService.getContainerLogById(containerId);
        containerLogUpdater.update(containerLog, request.toEntity());
    }

    public void deleteContainerLog(UUID containerId){
        ContainerLog containerLog = queryContainerLogService.getContainerLogById(containerId);
        containerLogDeleter.delete(containerLog);
    }

}
