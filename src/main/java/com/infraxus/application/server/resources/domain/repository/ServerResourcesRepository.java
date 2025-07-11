package com.infraxus.application.server.resources.domain.repository;

import com.infraxus.application.server.resources.domain.ServerResources;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ServerResourcesRepository extends CassandraRepository<ServerResources, UUID> {
}
