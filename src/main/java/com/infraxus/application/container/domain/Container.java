package com.infraxus.application.container.domain;

import com.infraxus.application.container.domain.value.ContainerKey;
import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table("container")
@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Container {

    @PrimaryKey
    private ContainerKey containerKey;

    private String dockerContainerId;

    private String containerName;

    private Integer buildCount;

    private String containerDescription;

    @CassandraType(type = CassandraType.Name.TEXT)
    private String containerState; // Enum

    private String externalIp;

    private String internalIp;

    private String externalPort;

    private String internalPort;

    private String githubLink;

    private String filePath;

    private LocalDateTime createAt;

    private String image;

    private LocalDateTime buildStartTime;

    private LocalDateTime buildEndTime;

    private Long buildDuration; // in milliseconds

}
