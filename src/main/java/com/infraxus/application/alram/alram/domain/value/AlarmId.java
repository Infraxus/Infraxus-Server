package com.infraxus.application.alram.alram.domain.value;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyClass;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;

import java.util.UUID;

@Getter
@Builder
@PrimaryKeyClass
public class AlarmId {
    @PrimaryKeyColumn(name = "containerId", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
    private UUID containerId;

    @PrimaryKeyColumn(name = "serverId", ordinal = 1, type = PrimaryKeyType.CLUSTERED)
    private UUID serverId;
}
