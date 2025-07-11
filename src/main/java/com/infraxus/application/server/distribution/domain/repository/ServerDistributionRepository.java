package com.infraxus.application.server.distribution.domain.repository;

import com.infraxus.application.server.distribution.domain.ServerDistribution;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ServerDistributionRepository extends CassandraRepository<ServerDistribution, UUID> {
}