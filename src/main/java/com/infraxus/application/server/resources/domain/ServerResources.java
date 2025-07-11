package com.infraxus.application.server.resources.domain;

import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table("server_resources")
@Getter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class ServerResources {

    @PrimaryKey
    private UUID serverId;

    private Integer cpuResources;

    private Integer memoryResources;

    private Integer storageResources;

    private Integer gpuResources;
}
