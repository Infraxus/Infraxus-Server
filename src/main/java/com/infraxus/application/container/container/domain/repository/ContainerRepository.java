package com.infraxus.application.container.container.domain.repository;

import com.infraxus.application.container.container.domain.Container;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;
import java.util.UUID;

@Repository
public interface ContainerRepository extends CassandraRepository<Container, UUID> {
    List<Container> findAllByServerId(UUID serverId);
    Optional<Container> findByDockerContainerId(String dockerContainerId);
}
