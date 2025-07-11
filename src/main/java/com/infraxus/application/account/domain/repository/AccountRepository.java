package com.infraxus.application.account.domain.repository;

import com.infraxus.application.account.domain.Account;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AccountRepository extends CassandraRepository<Account, UUID> {
}
