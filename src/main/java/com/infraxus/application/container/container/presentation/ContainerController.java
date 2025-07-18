package com.infraxus.application.container.container.presentation;

import com.infraxus.application.container.container.domain.Container;
import com.infraxus.application.container.container.presentation.dto.ContainerCreateRequest;
import com.infraxus.application.container.container.presentation.dto.ContainerUpdateRequest;
import com.infraxus.application.container.container.service.CommandContainerService;
import com.infraxus.application.container.container.service.QueryContainerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/servers/{serverId}/containers")
@RequiredArgsConstructor
public class ContainerController {

    private final CommandContainerService commandContainerService;
    private final QueryContainerService queryContainerService;

//    @PostMapping
//    public ResponseEntity<Void> createContainer(@PathVariable UUID serverId, @RequestBody ContainerCreateRequest request) {
//        // Ensure the request is associated with the correct serverId
//        // You might want to add a serverId field to ContainerCreateRequest or handle it in the service layer
//        commandContainerService.createContainer(request);
//        return ResponseEntity.ok().build();
//    }

    @GetMapping
    public ResponseEntity<List<Container>> getAllContainersByServer(@PathVariable UUID serverId) {
        return ResponseEntity.ok(queryContainerService.findAllByServerId(serverId));
    }

    @GetMapping("/{containerId}")
    public ResponseEntity<Container> getContainerById(@PathVariable UUID serverId, @PathVariable UUID containerId) {
        // Optional: Check if containerId belongs to serverId
        return ResponseEntity.ok(queryContainerService.findById(containerId));
    }

//    @PutMapping("/{containerId}")
//    public ResponseEntity<Void> updateContainer(@PathVariable UUID serverId, @PathVariable UUID containerId, @RequestBody ContainerUpdateRequest request) {
//        // Optional: Check if containerId belongs to serverId
//        commandContainerService.updateContainer(containerId, request);
//        return ResponseEntity.ok().build();
//    }

//    @DeleteMapping("/{containerId}")
//    public ResponseEntity<Void> deleteContainer(@PathVariable UUID serverId, @PathVariable UUID containerId) {
//        // Optional: Check if containerId belongs to serverId
//        commandContainerService.deleteContainer(containerId);
//        return ResponseEntity.ok().build();
//    }

    @PostMapping("/{containerId}/restart")
    public ResponseEntity<Void> restartContainer(@PathVariable UUID serverId, @PathVariable UUID containerId) {
        commandContainerService.restartContainer(containerId);
        return ResponseEntity.ok().build();
    }
}

