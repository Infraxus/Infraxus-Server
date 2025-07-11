package com.infraxus.application.server.distribution.service.implementation;

import com.infraxus.application.server.distribution.domain.ServerDistribution;
import com.infraxus.application.server.distribution.domain.repository.ServerDistributionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ServerDistributionReader {

    private final ServerDistributionRepository serverDistributionRepository;

    public List<ServerDistribution> findAll(){
        return serverDistributionRepository.findAll();
    }

    public ServerDistribution findById(UUID id){
        return serverDistributionRepository.findById(id)
                .orElse(null);
    }

}
