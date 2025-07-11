package com.infraxus.application.setting.domain.repository;

import com.infraxus.application.setting.domain.Setting;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SettingRepository extends CassandraRepository<Setting, UUID> {
}