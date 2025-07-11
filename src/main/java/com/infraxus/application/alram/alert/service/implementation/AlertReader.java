package com.infraxus.application.alram.alert.service.implementation;

import com.infraxus.application.alram.alert.domain.Alert;
import com.infraxus.application.alram.alert.domain.repository.AlertRepository;
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
        return alertRepository.findById(id)
                .orElse(null);
    }

}
