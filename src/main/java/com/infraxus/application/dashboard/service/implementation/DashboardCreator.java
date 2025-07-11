package com.infraxus.application.dashboard.service.implementation;

import com.infraxus.application.dashboard.domain.Dashboard;
import com.infraxus.application.dashboard.domain.repository.DashboardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DashboardCreator {

    private final DashboardRepository dashboardRepository;

    public void save(Dashboard dashboard){
        dashboardRepository.save(dashboard);
    }

}
