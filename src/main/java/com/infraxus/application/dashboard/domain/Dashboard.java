package com.infraxus.application.dashboard.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.util.UUID;

@Table("dashboard")
@Getter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Dashboard {

    @PrimaryKey
    private UUID dashboardId;

    private Integer totalServers;

    private Integer runningServers;

    private Integer errorServers;

    private Integer errorServerList;

    private Integer recentBuiltServerList;

    private Integer runningServerList;

}

