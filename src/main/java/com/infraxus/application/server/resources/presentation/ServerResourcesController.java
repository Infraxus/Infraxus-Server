package com.infraxus.application.server.resources.presentation;

import com.infraxus.application.server.resources.domain.ServerResources;
import com.infraxus.application.server.resources.presentation.dto.ServerResourcesCreateRequest;
import com.infraxus.application.server.resources.presentation.dto.ServerResourcesUpdateRequest;
import com.infraxus.application.server.resources.service.CommandServerResourcesService;
import com.infraxus.application.server.resources.service.QueryServerResourcesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/server-resources")
@RequiredArgsConstructor
public class ServerResourcesController {

    private final CommandServerResourcesService commandServerResourcesService;
    private final QueryServerResourcesService queryServerResourcesService;

    @PutMapping("/{serverId}")
    public void updateServerResources(
            @PathVariable UUID serverId,
            @RequestBody ServerResourcesUpdateRequest request
    ){
        commandServerResourcesService.updateServerResources(serverId, request);
    }

    @GetMapping
    public ResponseEntity<List<ServerResources>> getAllServerResources() {
        return ResponseEntity.ok(queryServerResourcesService.findAll());
    }

    @GetMapping("/{serverId}")
    public ResponseEntity<ServerResources> getServerResourcesById(@PathVariable UUID serverId) {
        return ResponseEntity.ok(queryServerResourcesService.findById(serverId));
    }
}
