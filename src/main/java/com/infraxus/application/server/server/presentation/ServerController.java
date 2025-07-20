package com.infraxus.application.server.server.presentation;

import com.infraxus.application.server.server.domain.Server;
import com.infraxus.application.server.server.presentation.dto.ServerCreateRequest;
import com.infraxus.application.server.server.service.CommandServerService;
import com.infraxus.application.server.server.service.MsaProjectService;
import com.infraxus.application.server.server.service.QueryServerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/servers")
@RequiredArgsConstructor
public class ServerController {

    private final CommandServerService commandServerService;
    private final QueryServerService queryServerService;
    private final MsaProjectService msaProjectService;

    @GetMapping("/{serverId}")
    public ResponseEntity<Server> getServerById(@PathVariable UUID serverId) {
        return ResponseEntity.ok(queryServerService.getServerById(serverId));
    }

    @GetMapping
    public ResponseEntity<List<Server>> getAllServers() {
        return ResponseEntity.ok(queryServerService.getAllServers());
    }

    @DeleteMapping("/{serverId}")
    public ResponseEntity<Void> deleteServer(@PathVariable UUID serverId) {
        commandServerService.deleteServer(serverId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{serverId}/startAll")
    public ResponseEntity<Void> startAllContainers(@PathVariable UUID serverId) {
        commandServerService.startAllContainersByServerId(serverId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{serverId}/stopAll")
    public ResponseEntity<Void> stopAllContainers(@PathVariable UUID serverId) {
        commandServerService.stopAllContainersByServerId(serverId);
        return ResponseEntity.ok().build();
    }

    @PostMapping
    public ResponseEntity<String> createServer(@RequestBody ServerCreateRequest request) {
        try {
            switch (request.getArchitectureType()) {
                case MSA:
                    msaProjectService.createNewMsaProject(request);
                    return ResponseEntity.ok("MSA project '" + request.getServerName() + "' created successfully.");
                case Monolithic:
                    try {
                        commandServerService.createServer(request);
                        return ResponseEntity.ok("Monolithic server '" + request.getServerName() + "' created successfully.");
                    } catch (IOException e) {
                        return ResponseEntity.badRequest().body("Failed to create monolithic server: " + e.getMessage());
                    }
                default:
                    return ResponseEntity.badRequest().body("Invalid architecture type.");
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to create server: " + e.getMessage());
        }
    }
}
