package com.infraxus.application.alarm.alarm.domain.repository;

import com.infraxus.application.alarm.alarm.domain.Alarm;
import com.infraxus.application.alarm.alarm.domain.value.AlarmId;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlarmRepository extends CassandraRepository<Alarm, AlarmId> {
}
