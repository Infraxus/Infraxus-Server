package com.infraxus.application.alarm.alert.domain.value;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyClass;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@PrimaryKeyClass
public class AlertKey implements Serializable {

    @PrimaryKeyColumn(name = "alertid", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
    private UUID alertId;

    @PrimaryKeyColumn(name = "containerid", ordinal = 1, type = PrimaryKeyType.CLUSTERED)
    private UUID containerId;

    @PrimaryKeyColumn(name = "serverid", ordinal = 2, type = PrimaryKeyType.CLUSTERED)
    private UUID serverId;

    public AlertKey(UUID alertId, UUID containerId, UUID serverId) {
        this.alertId = alertId;
        this.containerId = containerId;
        this.serverId = serverId;
    }
}
