package com.infraxus.application.dashboard.service.implementation;

import com.infraxus.application.dashboard.domain.Dashboard;
import com.infraxus.application.dashboard.domain.repository.DashboardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DashboardUpdater {

    private final DashboardRepository dashboardRepository;

    public void update(Dashboard updatableDashboard, Dashboard newDashboardData){
        Dashboard updated = updatableDashboard.toBuilder()
                .dashboardId(updatableDashboard.getDashboardId())
                .totalServers(newDashboardData.getTotalServers())
                .runningServers(newDashboardData.getRunningServers())
                .stopServers(newDashboardData.getStopServers())
                .stopServerList(newDashboardData.getStopServerList())
                .recentBuiltServerList(newDashboardData.getRecentBuiltServerList())
                .runningServerList(newDashboardData.getRunningServerList())
                .build();

        dashboardRepository.save(updated);
    }

}

