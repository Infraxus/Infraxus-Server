package com.infraxus.application.dashboard.domain.repository;

import com.infraxus.application.dashboard.domain.Dashboard;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface DashboardRepository extends CassandraRepository<Dashboard, UUID> {
}
