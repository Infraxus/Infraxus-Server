package com.infraxus.application.server.server.presentation.dto;

import com.infraxus.application.server.server.domain.Server;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
public class ServerUpdateRequest {
    private String architectureType;
    private String serverName;
    private String serverState;
    private List<String> skillStack;
    private Boolean rollBack;
    private LocalDateTime rebuildTime;

    @Builder
    public ServerUpdateRequest(String architectureType, String serverName, String serverState, List<String> skillStack, Boolean rollBack, LocalDateTime rebuildTime) {
        this.architectureType = architectureType;
        this.serverName = serverName;
        this.serverState = serverState;
        this.skillStack = skillStack;
        this.rollBack = rollBack;
        this.rebuildTime = rebuildTime;
    }

    public Server toEntity() {
        return Server.builder()
                .architectureType(this.architectureType)
                .serverName(this.serverName)
                .serverState(this.serverState)
                .skillStack(this.skillStack)
                .rollBack(this.rollBack)
                .rebuildTime(this.rebuildTime)
                .build();
    }
}
