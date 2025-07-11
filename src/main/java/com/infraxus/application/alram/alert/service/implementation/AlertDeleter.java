package com.infraxus.application.alram.alert.service.implementation;

import com.infraxus.application.alram.alert.domain.Alert;
import com.infraxus.application.alram.alert.domain.repository.AlertRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AlertDeleter {

    private final AlertRepository alertRepository;

    public void delete(Alert alert){
        alertRepository.delete(alert);
    }

}
