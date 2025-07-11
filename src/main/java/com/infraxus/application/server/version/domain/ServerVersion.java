package com.infraxus.application.server.version.domain;

import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table("server_version")
@Getter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class ServerVersion {

    @PrimaryKey
    private UUID serverId;

    private Integer switchingTiming;

    private Integer monitoringCriteria;

    private Integer trafficSplitRatio;

    private Integer switchingMethod;
}
