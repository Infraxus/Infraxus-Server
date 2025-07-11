package com.infraxus.application.server.version.presentation;

import com.infraxus.application.server.version.domain.ServerVersion;
import com.infraxus.application.server.version.presentation.dto.ServerVersionCreateRequest;
import com.infraxus.application.server.version.presentation.dto.ServerVersionUpdateRequest;
import com.infraxus.application.server.version.service.CommandServerVersionService;
import com.infraxus.application.server.version.service.QueryServerVersionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/server-versions")
@RequiredArgsConstructor
public class ServerVersionController {

    private final CommandServerVersionService commandServerVersionService;
    private final QueryServerVersionService queryServerVersionService;

    @PostMapping
    public ResponseEntity<Void> createServerVersion(@RequestBody ServerVersionCreateRequest request) {
        commandServerVersionService.createServerVersion(request);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{serverId}")
    public ResponseEntity<Void> updateServerVersion(@PathVariable UUID serverId, @RequestBody ServerVersionUpdateRequest request) {
        commandServerVersionService.updateServerVersion(serverId, request);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<ServerVersion>> getAllServerVersions() {
        return ResponseEntity.ok(queryServerVersionService.findAll());
    }

    @GetMapping("/{serverId}")
    public ResponseEntity<ServerVersion> getServerVersionById(@PathVariable UUID serverId) {
        return ResponseEntity.ok(queryServerVersionService.findById(serverId));
    }
}
