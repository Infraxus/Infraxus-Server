package com.infraxus.application.server.distribution.service;

import com.infraxus.application.server.distribution.domain.ServerDistribution;
import com.infraxus.application.server.distribution.presentation.dto.ServerDistributionCreateRequest;
import com.infraxus.application.server.distribution.presentation.dto.ServerDistributionUpdateRequest;
import com.infraxus.application.server.distribution.service.implementation.ServerDistributionCreator;
import com.infraxus.application.server.distribution.service.implementation.ServerDistributionDeleter;
import com.infraxus.application.server.distribution.service.implementation.ServerDistributionReader;
import com.infraxus.application.server.distribution.service.implementation.ServerDistributionUpdater;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CommandServerDistributionService {

    private final ServerDistributionCreator serverDistributionCreator;
    private final ServerDistributionUpdater serverDistributionUpdater;
    private final ServerDistributionDeleter serverDistributionDeleter;
    private final ServerDistributionReader serverDistributionReader;
    private final QueryServerDistributionService queryServerDistributionService;

    public void createServerDistribution(ServerDistributionCreateRequest request){
        serverDistributionCreator.save(request.toEntity());
    }

    public void updateServerDistribution(UUID serverId, ServerDistributionUpdateRequest request){
        ServerDistribution serverDistribution = queryServerDistributionService.getServerDistributionById(serverId);
        serverDistributionUpdater.update(serverDistribution, request.toEntity());
    }

    public void deleteServerDistribution(UUID serverId){
        ServerDistribution serverDistribution = queryServerDistributionService.getServerDistributionById(serverId);
        serverDistributionDeleter.delete(serverDistribution);
    }

}
