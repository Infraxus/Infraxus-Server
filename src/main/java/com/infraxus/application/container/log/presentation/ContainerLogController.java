package com.infraxus.application.container.log.presentation;

import com.infraxus.application.container.log.domain.ContainerLog;
import com.infraxus.application.container.log.presentation.dto.ContainerLogCreateRequest;
import com.infraxus.application.container.log.presentation.dto.ContainerLogUpdateRequest;
import com.infraxus.application.container.log.service.CommandContainerLogService;
import com.infraxus.application.container.log.service.QueryContainerLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/container-logs")
@RequiredArgsConstructor
public class ContainerLogController {

    private final QueryContainerLogService queryContainerLogService;

    @GetMapping
    public ResponseEntity<List<ContainerLog>> getAllContainerLogs() {
        return ResponseEntity.ok(queryContainerLogService.findAll());
    }

    @GetMapping("/{containerId}")
    public ResponseEntity<ContainerLog> getContainerLogById(@PathVariable UUID containerId) {
        return ResponseEntity.ok(queryContainerLogService.findById(containerId));
    }
}
