package com.infraxus.application.server.distribution.presentation;

import com.infraxus.application.server.distribution.domain.ServerDistribution;
import com.infraxus.application.server.distribution.presentation.dto.ServerDistributionCreateRequest;
import com.infraxus.application.server.distribution.presentation.dto.ServerDistributionUpdateRequest;
import com.infraxus.application.server.distribution.service.CommandServerDistributionService;
import com.infraxus.application.server.distribution.service.QueryServerDistributionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/server-distributions")
@RequiredArgsConstructor
public class ServerDistributionController {

    private final CommandServerDistributionService commandServerDistributionService;
    private final QueryServerDistributionService queryServerDistributionService;

    @PostMapping
    public ResponseEntity<Void> createServerDistribution(@RequestBody ServerDistributionCreateRequest request) {
        commandServerDistributionService.createServerDistribution(request);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{serverId}")
    public ResponseEntity<Void> updateServerDistribution(@PathVariable UUID serverId, @RequestBody ServerDistributionUpdateRequest request) {
        commandServerDistributionService.updateServerDistribution(serverId, request);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<ServerDistribution>> getAllServerDistributions() {
        return ResponseEntity.ok(queryServerDistributionService.findAll());
    }

    @GetMapping("/{serverId}")
    public ResponseEntity<ServerDistribution> getServerDistributionById(@PathVariable UUID serverId) {
        return ResponseEntity.ok(queryServerDistributionService.findById(serverId));
    }
}
