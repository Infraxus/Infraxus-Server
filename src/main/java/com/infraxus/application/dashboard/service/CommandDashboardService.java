package com.infraxus.application.dashboard.service;

import com.infraxus.application.dashboard.domain.Dashboard;
import com.infraxus.application.dashboard.presentation.dto.DashboardCreateRequest;
import com.infraxus.application.dashboard.presentation.dto.DashboardUpdateRequest;
import com.infraxus.application.dashboard.service.implementation.DashboardCreator;
import com.infraxus.application.dashboard.service.implementation.DashboardDeleter;
import com.infraxus.application.dashboard.service.implementation.DashboardReader;
import com.infraxus.application.dashboard.service.implementation.DashboardUpdater;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CommandDashboardService {

    private final DashboardCreator dashboardCreator;
    private final DashboardUpdater dashboardUpdater;
    private final DashboardDeleter dashboardDeleter;
    private final DashboardReader dashboardReader;
    private final QueryDashboardService queryDashboardService;

    public void createDashboard(DashboardCreateRequest request){
        dashboardCreator.save(request.toEntity());
    }

    public void updateDashboard(UUID dashboardId, DashboardUpdateRequest request){
        Dashboard dashboard = queryDashboardService.getDashboardById(dashboardId);
        dashboardUpdater.update(dashboard, request.toEntity());
    }

    public void deleteDashboard(UUID dashboardId){
        Dashboard dashboard = queryDashboardService.getDashboardById(dashboardId);
        dashboardDeleter.delete(dashboard);
    }

}
