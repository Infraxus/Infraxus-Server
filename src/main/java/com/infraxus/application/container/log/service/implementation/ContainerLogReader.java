package com.infraxus.application.container.log.service.implementation;

import com.infraxus.application.container.log.domain.ContainerLog;
import com.infraxus.application.container.log.domain.repository.ContainerLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ContainerLogReader {

    private final ContainerLogRepository containerLogRepository;

    public List<ContainerLog> findAll(){
        return containerLogRepository.findAll();
    }

    public ContainerLog findById(UUID id){
        return containerLogRepository.findById(id)
                .orElse(null);
    }

}
