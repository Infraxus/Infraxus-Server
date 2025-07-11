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
    private Integer errorServers;
    private Integer errorServerList;
    private Integer recentBuiltServerList;
    private Integer runningServerList;

    @Builder
    public DashboardUpdateRequest(Integer totalServers, Integer runningServers, Integer errorServers, Integer errorServerList, Integer recentBuiltServerList, Integer runningServerList) {
        this.totalServers = totalServers;
        this.runningServers = runningServers;
        this.errorServers = errorServers;
        this.errorServerList = errorServerList;
        this.recentBuiltServerList = recentBuiltServerList;
        this.runningServerList = runningServerList;
    }

    public Dashboard toEntity() {
        return Dashboard.builder()
                .totalServers(this.totalServers)
                .runningServers(this.runningServers)
                .errorServers(this.errorServers)
                .errorServerList(this.errorServerList)
                .recentBuiltServerList(this.recentBuiltServerList)
                .runningServerList(this.runningServerList)
                .build();
    }
}
