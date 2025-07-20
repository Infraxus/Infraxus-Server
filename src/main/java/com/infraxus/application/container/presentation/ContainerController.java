package com.infraxus.application.container.presentation;

import com.infraxus.application.container.domain.Container;
import com.infraxus.application.container.service.CommandContainerService;
import com.infraxus.application.container.service.QueryContainerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/servers/{serverId}/containers")
@RequiredArgsConstructor
public class ContainerController {

    private final CommandContainerService commandContainerService;
    private final QueryContainerService queryContainerService;

    @GetMapping
    public ResponseEntity<List<Container>> getAllContainersByServer(@PathVariable UUID serverId) {
        return ResponseEntity.ok(queryContainerService.findAllByServerId(serverId));
    }

    @GetMapping("/{containerId}")
    public ResponseEntity<Container> getContainerById(@PathVariable UUID serverId, @PathVariable UUID containerId) {
        // Optional: Check if containerId belongs to serverId
        return ResponseEntity.ok(queryContainerService.findById(containerId));
    }

    @PostMapping("/{containerId}/start")
    public ResponseEntity<Void> startContainer(@PathVariable UUID serverId, @PathVariable UUID containerId) {
        commandContainerService.startContainer(containerId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{containerId}/stop")
    public ResponseEntity<Void> stopContainer(@PathVariable UUID serverId, @PathVariable UUID containerId) {
        commandContainerService.stopContainer(containerId);
        return ResponseEntity.ok().build();
    }

}

