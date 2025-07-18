package com.infraxus.application.alarm.alert.domain.repository;

import com.infraxus.application.alarm.alert.domain.Alert;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AlertRepository extends CassandraRepository<Alert, UUID> {
    List<Alert> findAllByServerId(UUID serverId);
    List<Alert> findAllByContainerId(UUID containerId);
}
