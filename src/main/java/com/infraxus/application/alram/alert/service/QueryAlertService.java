package com.infraxus.application.alram.alert.service;

import com.infraxus.application.alram.alert.domain.Alert;
import com.infraxus.application.alram.alert.domain.exception.AlertNotFoundException;
import com.infraxus.application.alram.alert.service.implementation.AlertReader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class QueryAlertService {

    private final AlertReader alertReader;

    public List<Alert> findAll(){
        return alertReader.findAll();
    }

    public Alert findById(UUID id){
        return alertReader.findById(id);
    }

    public Alert getAlertById(UUID alertId) {
        Alert alert = alertReader.findById(alertId);
        if (alert == null) {
            throw new AlertNotFoundException("Alert with ID " + alertId + " not found.");
        }
        return alert;
    }
}
