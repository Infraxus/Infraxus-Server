package com.infraxus.application.dashboard.presentation;

import com.infraxus.application.dashboard.domain.Dashboard;
import com.infraxus.application.dashboard.presentation.dto.DashboardCreateRequest;
import com.infraxus.application.dashboard.presentation.dto.DashboardUpdateRequest;
import com.infraxus.application.dashboard.service.CommandDashboardService;
import com.infraxus.application.dashboard.service.QueryDashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/dashboards")
@RequiredArgsConstructor
public class DashboardController {

    private final QueryDashboardService queryDashboardService;

    @GetMapping
    public ResponseEntity<List<Dashboard>> getAllDashboards() {
        return ResponseEntity.ok(queryDashboardService.findAll());
    }

//    @GetMapping("/{dashboardId}")
//    public ResponseEntity<Dashboard> getDashboardById(@PathVariable UUID dashboardId) {
//        return ResponseEntity.ok(queryDashboardService.findById(dashboardId));
//    }
}
