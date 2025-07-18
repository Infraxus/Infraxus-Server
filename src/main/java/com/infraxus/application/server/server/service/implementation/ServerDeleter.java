package com.infraxus.application.server.server.service.implementation;

import com.infraxus.application.alarm.alert.domain.Alert;
import com.infraxus.application.alarm.alert.domain.repository.AlertRepository;
import com.infraxus.application.container.container.domain.Container;
import com.infraxus.application.container.container.domain.repository.ContainerRepository;
import com.infraxus.application.server.server.domain.Server;
import com.infraxus.application.server.server.domain.repository.ServerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ServerDeleter {

    private final ServerRepository serverRepository;
    private final ContainerRepository containerRepository;
    private final AlertRepository alertRepository;

    @Transactional
    public void delete(Server server) {
        // 1. Find all containers related to the server
        List<Container> containers = containerRepository.findAllByServerId(server.getServerId());

        // 2. For each container, find and delete all related alerts
        for (Container container : containers) {
            List<Alert> alerts = alertRepository.findAllByContainerId(container.getContainerId());
            alertRepository.deleteAll(alerts);
        }

        // 3. Delete all containers related to the server
        containerRepository.deleteAll(containers);

        // 4. Finally, delete the server itself
        serverRepository.delete(server);
    }
}
