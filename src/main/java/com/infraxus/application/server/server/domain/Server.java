package com.infraxus.application.server.server.domain;

import com.infraxus.application.server.server.domain.value.ArchitectureType;
import com.infraxus.application.server.server.domain.value.ServerState;
import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table("server")
@Getter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Server {

    @PrimaryKey
    private UUID serverId;

    @CassandraType(type = CassandraType.Name.TEXT)
    private String architectureType; // Enum

    private String serverName;

    @CassandraType(type = CassandraType.Name.TEXT)
    private String serverState; // Enum

    private List<String> skillStack;

    private Boolean rollBack;

    private LocalDateTime rebuildTime;

    private LocalDateTime createAt;

}
