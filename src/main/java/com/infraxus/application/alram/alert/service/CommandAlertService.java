package com.infraxus.application.alram.alert.service;

import com.infraxus.application.alram.alert.domain.Alert;
import com.infraxus.application.alram.alert.presentation.dto.AlertCreateRequest;
import com.infraxus.application.alram.alert.presentation.dto.AlertUpdateRequest;
import com.infraxus.application.alram.alert.service.implementation.AlertCreator;
import com.infraxus.application.alram.alert.service.implementation.AlertDeleter;
import com.infraxus.application.alram.alert.service.implementation.AlertReader;
import com.infraxus.application.alram.alert.service.implementation.AlertUpdater;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CommandAlertService {

    private final AlertCreator alertCreator;
    private final AlertUpdater alertUpdater;
    private final AlertDeleter alertDeleter;
    private final AlertReader alertReader;
    private final QueryAlertService queryAlertService;

    public void createAlert(AlertCreateRequest request){
        alertCreator.save(request.toEntity());
    }

    public void updateAlert(UUID alertId, AlertUpdateRequest request){
        Alert alert = queryAlertService.getAlertById(alertId);
        alertUpdater.update(alert, request.toEntity());
    }

    public void deleteAlert(UUID alertId){
        Alert alert = queryAlertService.getAlertById(alertId);
        alertDeleter.delete(alert);
    }

}
