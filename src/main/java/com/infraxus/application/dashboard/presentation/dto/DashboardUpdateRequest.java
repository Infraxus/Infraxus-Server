package com.infraxus.application.dashboard.presentation.dto;

import com.infraxus.application.dashboard.domain.Dashboard;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DashboardUpdateRequest {
    private Integer totalServers;
    private Integer runningServers;
    private Integer stopServers;
    private Integer stopServerList;
    private Integer recentBuiltServerList;
    private Integer runningServerList;

    @Builder
    public DashboardUpdateRequest(Integer totalServers, Integer runningServers, Integer stopServers, Integer stopServerList, Integer recentBuiltServerList, Integer runningServerList) {
        this.totalServers = totalServers;
        this.runningServers = runningServers;
        this.stopServers = stopServers;
        this.stopServerList = stopServerList;
        this.recentBuiltServerList = recentBuiltServerList;
        this.runningServerList = runningServerList;
    }

    public Dashboard toEntity() {
        return Dashboard.builder()
                .totalServers(this.totalServers)
                .runningServers(this.runningServers)
                .stopServers(this.stopServers)
                .stopServerList(this.stopServerList)
                .recentBuiltServerList(this.recentBuiltServerList)
                .runningServerList(this.runningServerList)
                .build();
    }
}
