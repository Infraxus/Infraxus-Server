package com.infraxus.application.server.distribution.domain;

import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table("server_distribution")
@Getter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class ServerDistribution {

    @PrimaryKey
    private UUID serverId;

    private Integer deploymentUnit;

    private Integer replacementWaitingTime;

    private Integer maxUnavailable;

    private Integer maxSurge;

    private Integer initialTrafficRatio;

    private Integer expansionRate;

    private Integer expansionCycle;

    private Integer autoStopThreshold;

    private String healthCheckPath;

    private Integer healthCheckCycle;

    private Integer maxFailuresAllowed;

    private Integer rollbackThreshold;
}
