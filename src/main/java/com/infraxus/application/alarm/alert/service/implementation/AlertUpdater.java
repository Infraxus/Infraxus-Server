package com.infraxus.application.alarm.alert.service.implementation;

import com.infraxus.application.alarm.alert.domain.Alert;
import com.infraxus.application.alarm.alert.domain.repository.AlertRepository;
import com.infraxus.application.alarm.alert.domain.value.AlertKey;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AlertUpdater {

    private final AlertRepository alertRepository;

    public void update(Alert updatableAlert, Alert newAlertData){
        Alert updated = updatableAlert.toBuilder()
                .alertKey(newAlertData.getAlertKey())
                .alertType(newAlertData.getAlertType())
                .alertTitle(newAlertData.getAlertTitle())
                .alertDescription(newAlertData.getAlertDescription())
                .build();

        alertRepository.save(updated);
    }

}
