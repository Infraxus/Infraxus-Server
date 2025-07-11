package com.infraxus.application.server.distribution.service.implementation;

import com.infraxus.application.server.distribution.domain.ServerDistribution;
import com.infraxus.application.server.distribution.domain.repository.ServerDistributionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ServerDistributionDeleter {

    private final ServerDistributionRepository serverDistributionRepository;

    public void delete(ServerDistribution serverDistribution){
        serverDistributionRepository.delete(serverDistribution);
    }

}
