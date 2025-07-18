package com.infraxus.application.container.container.service.implementation;

import com.infraxus.application.alarm.alert.domain.Alert;
import com.infraxus.application.alarm.alert.domain.repository.AlertRepository;
import com.infraxus.application.container.container.domain.Container;
import com.infraxus.application.container.container.domain.repository.ContainerRepository;
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
        List<Alert> alerts = alertRepository.findAllByContainerId(container.getContainerId());
        alertRepository.deleteAll(alerts);

        // 2. Delete the container itself
        containerRepository.delete(container);
    }
}
