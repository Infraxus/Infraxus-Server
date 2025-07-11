package com.infraxus.application.server.server.domain.repository;

import com.infraxus.application.server.server.domain.Server;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ServerRepository extends CassandraRepository<Server, UUID> {
}
