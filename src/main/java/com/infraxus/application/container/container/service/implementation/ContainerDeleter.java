package com.infraxus.application.container.container.service.implementation;

import com.infraxus.application.container.container.domain.Container;
import com.infraxus.application.container.container.domain.repository.ContainerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ContainerDeleter {

    private final ContainerRepository containerRepository;

    public void delete(Container container){
        containerRepository.delete(container);
    }

}
