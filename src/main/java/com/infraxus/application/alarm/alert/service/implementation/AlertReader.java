package com.infraxus.application.alarm.alert.service.implementation;

import com.infraxus.application.alarm.alert.domain.Alert;
import com.infraxus.application.alarm.alert.domain.repository.AlertRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AlertReader {

    private final AlertRepository alertRepository;

    public List<Alert> findAll(){
        return alertRepository.findAll();
    }

    public Alert findById(UUID id){
        return alertRepository.findByAlertKeyAlertId(id);
    }

    public List<Alert> findAllByContainerId(UUID containerId) {
        return alertRepository.findAllByAlertKeyContainerId(containerId);
    }

}
