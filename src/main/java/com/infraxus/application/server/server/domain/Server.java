package com.infraxus.application.server.server.domain;

import lombok.*;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("servers")
public class Server {
    @PrimaryKey
    private UUID serverId;
    private String serverName;
    private String serverType; // "monolithic" or "msa"
    private String jenkinsfilePath;
    private String dockerComposeFilePath; // For MSA, if using docker-compose
    private String serverState;
    private List<String> skillStack;
    private Boolean rollBack;
    private LocalDateTime rebuildTime;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static Server create(
            String serverName,
            String serverType,
            String jenkinsfilePath,
            String dockerComposeFilePath,
            List<String> skillStack,
            Boolean rollBack
    ) {
        return Server.builder()
                .serverId(UUID.randomUUID())
                .serverName(serverName)
                .serverType(serverType)
                .jenkinsfilePath(jenkinsfilePath)
                .dockerComposeFilePath(dockerComposeFilePath)
                .serverState(com.infraxus.application.server.server.domain.value.ServerState.PROVISIONING.name())
                .skillStack(skillStack) // Default skill stack
                .rollBack(rollBack)
                .rebuildTime(null)
                .createdAt(LocalDateTime.now())
                .updatedAt(null)
                .build();
    }
}

