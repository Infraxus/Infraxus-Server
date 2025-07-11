package com.infraxus.application.server.resources.domain.repository;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ServerResources extends CassandraRepository<ServerResources, UUID> {
}
