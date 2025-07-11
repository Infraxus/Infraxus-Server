package com.infraxus.application.alram.alram.domain.repository;

import com.infraxus.application.alram.alram.domain.Alarm;
import com.infraxus.application.alram.alram.domain.value.AlarmId;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlarmRepository extends CassandraRepository<Alarm, AlarmId> {
}