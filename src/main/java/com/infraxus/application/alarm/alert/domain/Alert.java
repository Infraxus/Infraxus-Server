package com.infraxus.application.alarm.alert.domain;

import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.util.Date;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table("alert")
@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Alert {

    @PrimaryKey
    private UUID alertId;

    private UUID containerId;

    private UUID serverId;

    @CassandraType(type = CassandraType.Name.TEXT)
    private String alertType; // Enum

    private String alertTitle;

    private String alertDescription;

    private Date createAt;
}
