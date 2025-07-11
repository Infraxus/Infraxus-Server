package com.infraxus.application.alram.alert.service.implementation;

import com.infraxus.application.alram.alert.domain.Alert;
import com.infraxus.application.alram.alert.domain.repository.AlertRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AlertUpdater {

    private final AlertRepository alertRepository;

    public void update(Alert updatableAlert, Alert newAlertData){
        updatableAlert.toBuilder()
                .containerId(newAlertData.getContainerId())
                .serverId(newAlertData.getServerId())
                .alertType(newAlertData.getAlertType())
                .alertTitle(newAlertData.getAlertTitle())
                .alertDescription(newAlertData.getAlertDescription())
                .build();

        alertRepository.save(updatableAlert);
    }

}
