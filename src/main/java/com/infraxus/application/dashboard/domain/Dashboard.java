package com.infraxus.application.dashboard.domain;

import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table("dashboard")
@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Dashboard {

    @PrimaryKey
    private UUID dashboardId;

    private Integer totalServers;

    private Integer runningServers;

    private Integer stopServers;

    private Integer stopServerList;

    private Integer recentBuiltServerList;

    private Integer runningServerList;

}

