package com.infraxus.application.container.domain.value;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyClass;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;

import java.util.UUID;

@Getter
@Setter
@PrimaryKeyClass
public class ContainerKey {
    @PrimaryKeyColumn(name = "serverid", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
    private UUID serverId;

    @PrimaryKeyColumn(name = "containerid", ordinal = 1, type = PrimaryKeyType.CLUSTERED)
    private UUID containerId;

    public ContainerKey(UUID serverId, UUID containerId) {
        this.serverId = serverId;
        this.containerId = containerId;
    }
}
