package com.infraxus.application.dashboard.service.implementation;

import com.infraxus.application.dashboard.domain.Dashboard;
import com.infraxus.application.dashboard.domain.repository.DashboardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DashboardDeleter {

    private final DashboardRepository dashboardRepository;

    public void delete(Dashboard dashboard){
        dashboardRepository.delete(dashboard);
    }

}
