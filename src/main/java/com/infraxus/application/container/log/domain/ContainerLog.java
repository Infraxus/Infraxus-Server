package com.infraxus.application.container.log.domain;

import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.util.Date;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table("container_log")
@Getter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class ContainerLog {

    @PrimaryKey
    private UUID containerId;

    private UUID serverId;

    @CassandraType(type = CassandraType.Name.TEXT)
    private String logType;

    private String logLevel;

    private String logTitle;

    private String logContent;

    private String stackTrace;

    private Date createAt;

}
