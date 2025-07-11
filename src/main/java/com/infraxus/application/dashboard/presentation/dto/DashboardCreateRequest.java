package com.infraxus.application.dashboard.presentation.dto;

import com.infraxus.application.dashboard.domain.Dashboard;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
public class DashboardCreateRequest {
    private Integer totalServers;
    private Integer runningServers;
    private Integer errorServers;
    private Integer errorServerList;
    private Integer recentBuiltServerList;
    private Integer runningServerList;

    @Builder
    public DashboardCreateRequest(Integer totalServers, Integer runningServers, Integer errorServers, Integer errorServerList, Integer recentBuiltServerList, Integer runningServerList) {
        this.totalServers = totalServers;
        this.runningServers = runningServers;
        this.errorServers = errorServers;
        this.errorServerList = errorServerList;
        this.recentBuiltServerList = recentBuiltServerList;
        this.runningServerList = runningServerList;
    }

    public Dashboard toEntity() {
        return Dashboard.builder()
                .dashboardId(UUID.randomUUID())
                .totalServers(this.totalServers)
                .runningServers(this.runningServers)
                .errorServers(this.errorServers)
                .errorServerList(this.errorServerList)
                .recentBuiltServerList(this.recentBuiltServerList)
                .runningServerList(this.runningServerList)
                .build();
    }
}

