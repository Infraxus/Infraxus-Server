package com.infraxus.application.dashboard.service;

import com.infraxus.application.dashboard.domain.Dashboard;
import com.infraxus.application.dashboard.domain.exception.DashboardNotFoundException;
import com.infraxus.application.dashboard.service.implementation.DashboardReader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class QueryDashboardService {

    private final DashboardReader dashboardReader;

    public List<Dashboard> findAll(){
        return dashboardReader.findAll();
    }

    public Dashboard findById(UUID id){
        return dashboardReader.findById(id);
    }

    public Dashboard getDashboardById(UUID dashboardId) {
        Dashboard dashboard = dashboardReader.findById(dashboardId);
        if (dashboard == null) {
            throw new DashboardNotFoundException("Dashboard with ID " + dashboardId + " not found.");
        }
        return dashboard;
    }
}
