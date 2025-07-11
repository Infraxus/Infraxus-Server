package com.infraxus.application.alram.alert.domain.repository;

import com.infraxus.application.alram.alert.domain.Alert;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AlertRepository extends CassandraRepository<Alert, UUID> {
}