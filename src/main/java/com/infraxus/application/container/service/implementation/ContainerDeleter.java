package com.infraxus.application.container.service.implementation;

import com.infraxus.application.alarm.alert.domain.Alert;
import com.infraxus.application.alarm.alert.domain.repository.AlertRepository;
import com.infraxus.application.container.domain.Container;
import com.infraxus.application.container.domain.repository.ContainerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ContainerDeleter {

    private final ContainerRepository containerRepository;
    private final AlertRepository alertRepository;

    @Transactional
    public void delete(Container container) {
        // 1. Find and delete all alerts related to the container
        List<Alert> alerts = alertRepository.findAllByAlertKeyContainerId(container.getContainerKey().getContainerId());
        alertRepository.deleteAll(alerts);

        // 2. Delete the container itself
        containerRepository.delete(container);
    }
}
