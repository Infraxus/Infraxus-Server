package com.infraxus.application.alarm.alert.domain.repository;

import com.infraxus.application.alarm.alert.domain.Alert;
import com.infraxus.application.alarm.alert.domain.value.AlertKey;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AlertRepository extends CassandraRepository<Alert, AlertKey> {

    List<Alert> findAllByAlertKeyServerId(UUID serverId);

    @Query("SELECT * FROM alert WHERE containerid = ?0 ALLOW FILTERING")
    List<Alert> findAllByAlertKeyContainerId(UUID containerId);

    Alert findByAlertKeyAlertId(UUID alertKeyAlertId);
}
