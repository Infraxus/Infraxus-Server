package com.infraxus.application.server.distribution.service;

import com.infraxus.application.server.distribution.domain.ServerDistribution;
import com.infraxus.application.server.distribution.domain.exception.ServerDistributionNotFoundException;
import com.infraxus.application.server.distribution.service.implementation.ServerDistributionReader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class QueryServerDistributionService {

    private final ServerDistributionReader serverDistributionReader;

    public List<ServerDistribution> findAll(){
        return serverDistributionReader.findAll();
    }

    public ServerDistribution findById(UUID id){
        return serverDistributionReader.findById(id);
    }

    public ServerDistribution getServerDistributionById(UUID serverId) {
        ServerDistribution serverDistribution = serverDistributionReader.findById(serverId);
        if (serverDistribution == null) {
            throw new ServerDistributionNotFoundException("ServerDistribution with ID " + serverId + " not found.");
        }
        return serverDistribution;
    }
}
