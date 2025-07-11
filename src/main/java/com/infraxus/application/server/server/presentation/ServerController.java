package com.infraxus.application.server.server.presentation;

import com.infraxus.application.server.server.domain.Server;
import com.infraxus.application.server.server.presentation.dto.ServerCreateRequest;
import com.infraxus.application.server.server.presentation.dto.ServerUpdateRequest;
import com.infraxus.application.server.server.service.CommandServerService;
import com.infraxus.application.server.server.service.QueryServerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/servers")
@RequiredArgsConstructor
public class ServerController {

    private final CommandServerService commandServerService;
    private final QueryServerService queryServerService;

    @PostMapping
    public ResponseEntity<Void> createServer(@RequestBody ServerCreateRequest request) {
        commandServerService.createServer(request);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{serverId}")
    public ResponseEntity<Void> updateServer(@PathVariable UUID serverId, @RequestBody ServerUpdateRequest request) {
        commandServerService.updateServer(serverId, request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{serverId}")
    public ResponseEntity<Void> deleteServer(@PathVariable UUID serverId) {
        commandServerService.deleteServer(serverId);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<Server>> getAllServers() {
        return ResponseEntity.ok(queryServerService.findAll());
    }

    @GetMapping("/{serverId}")
    public ResponseEntity<Server> getServerById(@PathVariable UUID serverId) {
        return ResponseEntity.ok(queryServerService.findById(serverId));
    }
}
