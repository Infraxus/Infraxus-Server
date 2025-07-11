package com.infraxus.application.container.container.presentation;

import com.infraxus.application.container.container.domain.Container;
import com.infraxus.application.container.container.presentation.dto.ContainerCreateRequest;
import com.infraxus.application.container.container.presentation.dto.ContainerUpdateRequest;
import com.infraxus.application.container.container.service.CommandContainerService;
import com.infraxus.application.container.container.service.QueryContainerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/containers")
@RequiredArgsConstructor
public class ContainerController {

    private final CommandContainerService commandContainerService;
    private final QueryContainerService queryContainerService;

    @PutMapping("/{containerId}")
    public void updateContainer(
            @PathVariable UUID containerId,
            @RequestBody ContainerUpdateRequest request
    ) {
        commandContainerService.updateContainer(containerId, request);
    }

    @GetMapping
    public ResponseEntity<List<Container>> getAllContainers() {
        return ResponseEntity.ok(queryContainerService.findAll());
    }

    @GetMapping("/{containerId}")
    public ResponseEntity<Container> getContainerById(@PathVariable UUID containerId) {
        return ResponseEntity.ok(queryContainerService.findById(containerId));
    }
}
