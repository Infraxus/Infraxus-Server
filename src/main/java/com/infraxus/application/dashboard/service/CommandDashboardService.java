package com.infraxus.application.dashboard.service;

import com.infraxus.application.dashboard.domain.Dashboard;
import com.infraxus.application.dashboard.domain.repository.DashboardRepository;
import com.infraxus.application.dashboard.presentation.dto.DashboardCreateRequest;
import com.infraxus.application.dashboard.presentation.dto.DashboardUpdateRequest;
import com.infraxus.application.dashboard.service.implementation.DashboardCreator;
import com.infraxus.application.dashboard.service.implementation.DashboardDeleter;
import com.infraxus.application.dashboard.service.implementation.DashboardReader;
import com.infraxus.application.dashboard.service.implementation.DashboardUpdater;
import com.infraxus.application.server.server.domain.Server;
import com.infraxus.application.server.server.domain.repository.ServerRepository;
import com.infraxus.global.loki.service.LoggingService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CommandDashboardService {

    private final DashboardCreator dashboardCreator;
    private final DashboardUpdater dashboardUpdater;
    private final DashboardDeleter dashboardDeleter;
    private final DashboardReader dashboardReader;
    private final QueryDashboardService queryDashboardService;
    private final LoggingService loggingService;
    private final ServerRepository serverRepository;
    private final DashboardRepository dashboardRepository;

    public void createDashboard(DashboardCreateRequest request){
        dashboardCreator.save(request.toEntity());
        loggingService.logEvent("Dashboard created");
    }

    public void updateDashboard(UUID dashboardId, DashboardUpdateRequest request){
        Dashboard dashboard = queryDashboardService.getDashboardById(dashboardId);
        dashboardUpdater.update(dashboard, request.toEntity());
        loggingService.logEvent("Dashboard updated: " + dashboardId);
    }

    public void deleteDashboard(UUID dashboardId){
        Dashboard dashboard = queryDashboardService.getDashboardById(dashboardId);
        dashboardDeleter.delete(dashboard);
        loggingService.logEvent("Dashboard deleted: " + dashboardId);
    }

    @Scheduled(fixedRate = 60000) // Every 1 minute
    public void updateDashboardMetrics() {
        loggingService.logEvent("Updating dashboard metrics...");

        List<Server> allServers = serverRepository.findAll();

        int totalServers = allServers.size();
        int runningServers = 0;
        int errorServers = 0;
        int recentBuiltServers = 0; // Assuming this means newly created or built servers

        for (Server server : allServers) {
            // Assuming ServerState enum or similar for server status
            if ("RUNNING".equals(server.getServerState())) { // Using serverState for actual state
                runningServers++;
            } else if ("ERROR".equals(server.getServerState())) { // Using serverState for actual state
                errorServers++;
            }
            // Logic for recentBuiltServers would depend on a timestamp or specific flag in Server entity
            // For now, we'll just count them as 0 or implement a placeholder.
        }

        // Fetch the existing dashboard or create a new one if it doesn't exist
        // Assuming there's only one dashboard or a way to identify the main one
        Optional<Dashboard> existingDashboard = dashboardRepository.findAll().stream().findFirst();
        Dashboard dashboard;

        if (existingDashboard.isPresent()) {
            dashboard = existingDashboard.get();
        } else {
            dashboard = Dashboard.builder()
                    .dashboardId(UUID.randomUUID())
                    .build();
        }

        dashboard.setTotalServers(totalServers);
        dashboard.setRunningServers(runningServers);
        dashboard.setErrorServers(errorServers);
        dashboard.setRecentBuiltServerList(recentBuiltServers); // Placeholder
        dashboard.setRunningServerList(runningServers); // Assuming this is the same as runningServers count
        dashboard.setErrorServerList(errorServers); // Assuming this is the same as errorServers count

        dashboardRepository.save(dashboard);
        loggingService.logEvent("Dashboard metrics updated. Total Servers: " + totalServers + ", Running: " + runningServers + ", Error: " + errorServers);
    }
}
