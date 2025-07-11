package com.infraxus.application.container.log.domain.repository;

import com.infraxus.application.container.log.domain.ContainerLog;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ContainerLogRepository extends CassandraRepository<ContainerLog, UUID> {
}