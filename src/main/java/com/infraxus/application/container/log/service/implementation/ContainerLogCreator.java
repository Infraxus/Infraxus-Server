package com.infraxus.application.container.log.service.implementation;

import com.infraxus.application.container.log.domain.ContainerLog;
import com.infraxus.application.container.log.domain.repository.ContainerLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ContainerLogCreator {

    private final ContainerLogRepository containerLogRepository;

    public void save(ContainerLog containerLog){
        containerLogRepository.save(containerLog);
    }

}
