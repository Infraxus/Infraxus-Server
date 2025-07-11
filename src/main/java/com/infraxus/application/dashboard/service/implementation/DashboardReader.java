package com.infraxus.application.dashboard.service.implementation;

import com.infraxus.application.dashboard.domain.Dashboard;
import com.infraxus.application.dashboard.domain.repository.DashboardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DashboardReader {

    private final DashboardRepository dashboardRepository;

    public List<Dashboard> findAll(){
        return dashboardRepository.findAll();
    }

    public Dashboard findById(UUID id){
        return dashboardRepository.findById(id)
                .orElse(null);
    }

}
