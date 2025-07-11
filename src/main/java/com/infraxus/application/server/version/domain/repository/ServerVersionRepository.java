package com.infraxus.application.server.version.domain.repository;

import com.infraxus.application.server.version.domain.ServerVersion;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ServerVersionRepository extends CassandraRepository<ServerVersion, UUID> {
}