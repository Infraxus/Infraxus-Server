package com.infraxus.application.container.service.implementation;

import com.infraxus.application.container.domain.Container;
import com.infraxus.application.container.domain.repository.ContainerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ContainerCreator {

    private final ContainerRepository containerRepository;

    public void save(Container container){
        containerRepository.save(container);
    }

}
