package com.infraxus.application.container.log.service;

import com.infraxus.application.container.log.domain.ContainerLog;
import com.infraxus.application.container.log.domain.exception.ContainerLogNotFoundException;
import com.infraxus.application.container.log.service.implementation.ContainerLogReader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class QueryContainerLogService {

    private final ContainerLogReader containerLogReader;

    public List<ContainerLog> findAll(){
        return containerLogReader.findAll();
    }

    public ContainerLog findById(UUID id){
        return containerLogReader.findById(id);
    }

    public ContainerLog getContainerLogById(UUID containerId) {
        ContainerLog containerLog = containerLogReader.findById(containerId);
        if (containerLog == null) {
            throw new ContainerLogNotFoundException("ContainerLog with ID " + containerId + " not found.");
        }
        return containerLog;
    }
}
