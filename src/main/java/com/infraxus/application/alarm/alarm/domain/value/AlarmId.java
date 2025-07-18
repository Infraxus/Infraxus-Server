package com.infraxus.application.alarm.alarm.domain.value;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyClass;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;

import java.util.UUID;

@Getter
@Setter
@Builder
@PrimaryKeyClass
public class AlarmId {
    @PrimaryKeyColumn(name = "containerId", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
    private UUID containerId;

    @PrimaryKeyColumn(name = "serverId", ordinal = 1, type = PrimaryKeyType.CLUSTERED)
    private UUID serverId;

    public AlarmId(UUID containerId, UUID serverId) {
        this.containerId = containerId;
        this.serverId = serverId;
    }
}
